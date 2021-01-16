package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.UsersApi;
import ch.heigvd.amt.api.model.*;
import ch.heigvd.amt.api.model.User;
import ch.heigvd.amt.entities.ApiKeyEntity;
import ch.heigvd.amt.entities.BadgeEntity;
import ch.heigvd.amt.entities.DTOs.AwardAmountDTO;
import ch.heigvd.amt.entities.PointScaleEntity;
import ch.heigvd.amt.entities.UserEntity;
import ch.heigvd.amt.entities.awards.BadgeAwardEntity;
import ch.heigvd.amt.entities.awards.PointScaleAwardEntity;
import ch.heigvd.amt.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UsersController implements UsersApi {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ServletRequest servletRequest;

    @Autowired
    BadgeAwardRepository badgeAwardRepository;

    @Autowired
    PointScaleAwardRepository pointScaleAwardRepository;

    @Autowired
    BadgeRepository badgeRepository;

    @Autowired
    PointScaleRepository pointScaleRepository;

    @Autowired
    ApiKeyRepository apiKeyRepository;

    /**
     * Servlet entry point GET users/id
     * @param id user's Id
     * @return response
     */
    @Override
    public ResponseEntity<User> getUser(String id) {
        String apiKeyId = (String) servletRequest.getAttribute("Application");

        ApiKeyEntity apiKey = apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ApiKey not found"));

        UserEntity existingUserEntity =
                userRepository.findByApiKeyEntityValue_AndId(apiKeyId,id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return ResponseEntity.ok(toUser(existingUserEntity,apiKey));
    }

    /**
     * Servlet entry point GET users
     * @return response
     */
    @Override
    public ResponseEntity<List<User>> getUsers() {

        String apiKeyId = (String) servletRequest.getAttribute("Application");

        ApiKeyEntity apiKey = apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ApiKey not found"));

        Optional<List<UserEntity>> userEntities = userRepository.findByApiKeyEntityValue(apiKeyId);
        List<User> users = new ArrayList<>();

        if (userEntities.isPresent()) {
            for (UserEntity userEntity : userEntities.get()) {
                users.add(toUser(userEntity,apiKey));
            }
        }

        return ResponseEntity.ok(users);
    }

    /**
     * Converts a user entity to a user
     * @param entity : user entity
     * @return user
     */
    public User toUser(UserEntity entity, ApiKeyEntity apiKey) {
        User user = new User();
        user.setId(entity.getId());

        //badgesAmounts
        List<AwardAmountDTO> badgeAwardAmounts = badgeAwardRepository.getAmounts(entity.getId());
        List<UserBadgesAmount> badgeAmounts = new ArrayList<>();
        for(AwardAmountDTO badgeAwardAmount : badgeAwardAmounts){

            BadgeEntity badge = badgeRepository.findByApiKeyEntityValue_AndId(apiKey.getValue(),
                    Long.parseLong(badgeAwardAmount.getPath().substring("/badges/".length())))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

            UserBadgesAmount userPointScaleAmount = new UserBadgesAmount();
            userPointScaleAmount.setAmount(badgeAwardAmount.getAmount());
            userPointScaleAmount.setName(badge.getName());
            badgeAmounts.add(userPointScaleAmount);
        }
        user.setBadgesAmount(badgeAmounts);

        //pointScaleAmounts
        List<AwardAmountDTO> pointScaleAwardAmounts = pointScaleAwardRepository.getAmounts(entity.getId());
        List<UserBadgesAmount> pointScaleAmounts = new ArrayList<>();
        for(AwardAmountDTO pointScaleAwardAmount : pointScaleAwardAmounts){

            PointScaleEntity pointScale = pointScaleRepository.findByApiKeyEntityValue_AndId(apiKey.getValue(),
                    Long.parseLong(pointScaleAwardAmount.getPath().substring("/pointScales/".length())))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

            UserBadgesAmount userPointScaleAmount = new UserBadgesAmount();
            userPointScaleAmount.setAmount(pointScaleAwardAmount.getAmount());
            userPointScaleAmount.setName(pointScale.getName());
            pointScaleAmounts.add(userPointScaleAmount);
        }
        user.setPointScalesAmount(pointScaleAmounts);

        //badgesAwards
        Optional<List<BadgeAwardEntity>> badgesAwardsInRep =
                badgeAwardRepository.findByUser_AndApiKeyEntity(entity,apiKey);
        List<BadgeAwardEntity> badgesAwards = new ArrayList<>();
        if(badgesAwardsInRep.isPresent()){
            badgesAwards = badgesAwardsInRep.get();
        }

        //pointScaleAwards
        Optional<List<PointScaleAwardEntity>> pointScalesAwardsInRep =
                pointScaleAwardRepository.findByUser_AndApiKeyEntity(entity,apiKey);
        List<PointScaleAwardEntity> pointScalesAwards = new ArrayList<>();
        if(pointScalesAwardsInRep.isPresent()){
            pointScalesAwards = pointScalesAwardsInRep.get();
        }

        user.setBadgesAwards(badgesAwards.stream().map(badgeAwardEntity -> {
            BadgeAward badgeAward = new BadgeAward();
            badgeAward.setPath(URI.create(badgeAwardEntity.getPath()));
            badgeAward.setReason(badgeAwardEntity.getReason());
            badgeAward.setTimestamp(badgeAwardEntity.getTimestamp());
            return badgeAward;
        }).collect(Collectors.toList()));

        user.setPointsAwards(pointScalesAwards.stream().map(pointsAwardEntity -> {
            PointScaleAward pointscaleAward = new PointScaleAward();
            pointscaleAward.setAmount(pointsAwardEntity.getAmount());
            pointscaleAward.setPath(URI.create(pointsAwardEntity.getPath()));
            pointscaleAward.setReason(pointsAwardEntity.getReason());
            pointscaleAward.setTimestamp(pointsAwardEntity.getTimestamp());
            return pointscaleAward;
        }).collect(Collectors.toList()));

        return user;
    }

    /**
     * get a new user entity
     * @param id string id
     * @return user entity
     */
    public static UserEntity toUserEntity(String id) {
        UserEntity entity = new UserEntity();
        entity.setId(id);
        return entity;
    }
}
