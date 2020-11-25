package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.UsersApi;
import ch.heigvd.amt.api.model.User;
import ch.heigvd.amt.api.model.User;
import ch.heigvd.amt.api.model.User;
import ch.heigvd.amt.entities.UserEntity;
import ch.heigvd.amt.entities.UserEntity;
import ch.heigvd.amt.entities.UserEntity;
import ch.heigvd.amt.repositories.ApiKeyRepository;
import ch.heigvd.amt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UsersController implements UsersApi {

    @Autowired
    UserRepository userRepository;

    @Override
    public ResponseEntity<User> getUser(String id) {
        UserEntity existingUserEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(toUser(existingUserEntity));
    }

    @Override
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = new ArrayList<>();
        for (UserEntity userEntity : userRepository.findAll()) {
            users.add(toUser(userEntity));
        }
        return ResponseEntity.ok(users);
    }

    /**
     * Converts a user entity to a user
     * @param entity : user entity
     * @return user
     */
    private User toUser(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
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
