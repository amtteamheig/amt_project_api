package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.UsersApi;
import ch.heigvd.amt.api.model.*;
import ch.heigvd.amt.api.model.User;
import ch.heigvd.amt.api.model.User;
import ch.heigvd.amt.api.model.User;
import ch.heigvd.amt.entities.UserEntity;
import ch.heigvd.amt.entities.UserEntity;
import ch.heigvd.amt.entities.UserEntity;
import ch.heigvd.amt.entities.UserEntity;
import ch.heigvd.amt.entities.awards.BadgeAwardEntity;
import ch.heigvd.amt.entities.awards.PointScaleAwardEntity;
import ch.heigvd.amt.repositories.ApiKeyRepository;
import ch.heigvd.amt.repositories.BadgeAwardRepository;
import ch.heigvd.amt.repositories.PointScaleAwardRepository;
import ch.heigvd.amt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletRequest;
import javax.swing.text.html.Option;
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

    /**
     * Servlet entry point GET users/id
     * @param id user's Id
     * @return response
     */
    @Override
    public ResponseEntity<User> getUser(String id) {
        String apiKeyId = (String) servletRequest.getAttribute("Application");

        UserEntity existingUserEntity =
                userRepository.findByApiKeyEntityValue_AndId(apiKeyId,id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(toUser(existingUserEntity,badgeAwardRepository,pointScaleAwardRepository));
    }

    /**
     * Servlet entry point GET users
     * @return response
     */
    @Override
    public ResponseEntity<List<User>> getUsers() {

        String apiKeyId = (String) servletRequest.getAttribute("Application");
        Optional<List<UserEntity>> userEntities = userRepository.findByApiKeyEntityValue(apiKeyId);
        List<User> users = new ArrayList<>();

        if (userEntities.isPresent()) {
            for (UserEntity userEntity : userEntities.get()) {
                users.add(toUser(userEntity,badgeAwardRepository,pointScaleAwardRepository));
            }
        }

        return ResponseEntity.ok(users);
    }

    /**
     * Converts a user entity to a user
     * @param entity : user entity
     * @return user
     */
    public static User toUser(UserEntity entity, BadgeAwardRepository BAR, PointScaleAwardRepository PAR) {
        User user = new User();
        user.setId(entity.getId());

        Optional<List<BadgeAwardEntity>> badgesAwardsInRep = BAR.findByUser(entity);
        List<BadgeAwardEntity> badgesAwards = new ArrayList<>();
        if(badgesAwardsInRep.isPresent()){
            badgesAwards = badgesAwardsInRep.get();
        }

        Optional<List<PointScaleAwardEntity>> pointScalesAwardsInRep = PAR.findByUser(entity);
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
