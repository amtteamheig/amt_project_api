package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.EventsApi;
import ch.heigvd.amt.api.exceptions.ApiException;
import ch.heigvd.amt.api.model.APIError;
import ch.heigvd.amt.api.model.Event;
import ch.heigvd.amt.api.model.User;
import ch.heigvd.amt.entities.ApiKeyEntity;
import ch.heigvd.amt.entities.RuleEntity;
import ch.heigvd.amt.entities.UserEntity;
import ch.heigvd.amt.entities.awards.BadgeAwardEntity;
import ch.heigvd.amt.entities.awards.PointScaleAwardEntity;
import ch.heigvd.amt.repositories.ApiKeyRepository;
import ch.heigvd.amt.repositories.RuleRepository;
import ch.heigvd.amt.repositories.UserRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
public class EventsProcessorService implements EventsApi {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Autowired
    RuleRepository ruleRepository;

    @Autowired
    ServletRequest servletRequest;


    /**
     * Servlet entry point
     * @param event (optional) event object built by user
     * @return response
     */
    @Override
    public ResponseEntity<Void> eventProcess(@Valid Event event) {

        String apiKeyId = (String) servletRequest.getAttribute("Application");

        ApiKeyEntity apiKey = apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        try {
            checkEvent(event);
        } catch(ApiException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
        }

        Optional<UserEntity> userInRep = userRepository.findByApiKeyEntityValue_AndId(apiKeyId, event.getUserId());
        UserEntity user;

        //check if user is already in database or not
        if(userInRep.isPresent()){
            user = userInRep.get();
        } else {
            user = UsersController.toUserEntity(event.getUserId());
            user.setApiKeyEntity(apiKey);
            userRepository.save(user);
        }

        //check rules
        try {
            handleRules(event,user,apiKeyId);
            userRepository.save(user);
        } catch(ApiException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
        }

        return ResponseEntity.ok().build();
    }

    /**
     * handles rules
     * @param event current event
     * @param user current user
     * @return true if a rule with event type was found
     */
    private void handleRules(Event event, UserEntity user, String apiKeyId) throws ApiException{

        Optional<RuleEntity> ruleInRep = ruleRepository.findBy_if_TypeAndApiKeyEntityValue(event.getType(),apiKeyId);

        //if no rules found with given type, return
        if(ruleInRep.isEmpty()){
            throw new ApiException(400, "Cannot find Rule with type " + event.getType());
        }

        //init
        PointScaleAwardEntity pointScaleAwardEntity = new PointScaleAwardEntity();
        BadgeAwardEntity badgeAwardEntity = new BadgeAwardEntity();
        RuleEntity rule = ruleInRep.get();

        //set pointScale award
        pointScaleAwardEntity.setPath(rule.get_then().getAwardPoints().getPointScale());
        pointScaleAwardEntity.setReason(rule.get_if().getType());
        pointScaleAwardEntity.setTimestamp(event.getTimestamp());
        pointScaleAwardEntity.setAmount(rule.get_then().getAwardPoints().getAmount());

        //set badge award
        badgeAwardEntity.setPath(rule.get_then().getAwardBadge());
        badgeAwardEntity.setReason(rule.get_if().getType());
        badgeAwardEntity.setTimestamp(event.getTimestamp());

        //update user
        user.getBadgesAwards().add(badgeAwardEntity);
        user.getPointsAwards().add(pointScaleAwardEntity);
    }

    /**
     * Check if the attributes of the event are correct
     * @param event
     * @throws ApiException
     */
    private void checkEvent(Event event) throws ApiException {
        if(event.getUserId() == null) {
            throw new ApiException(400, "UserID is null");
        }

        if(event.getTimestamp() == null) {
            throw new ApiException(400, "Timestamp is null");
        }

        if(event.getType() == null || event.getType().isEmpty()) {
            throw new ApiException(400, "Type is empty");
        }
    }

}
