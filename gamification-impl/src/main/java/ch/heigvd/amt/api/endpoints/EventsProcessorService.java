package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.EventsApi;
import ch.heigvd.amt.api.model.Event;
import ch.heigvd.amt.entities.UserEntity;
import ch.heigvd.amt.repositories.ApiKeyRepository;
import ch.heigvd.amt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class EventsProcessorService implements EventsApi {

    @Autowired
    UserRepository userRepository;

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> eventProcess(@Valid Event event) {

        UserEntity user = toUserEntity(event.getUserId());

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    /**
     * get a new user entity
     * @param id string id
     * @return user entity
     */
    private UserEntity toUserEntity(String id) {
        UserEntity entity = new UserEntity();
        entity.setId(id);
        return entity;
    }

}
