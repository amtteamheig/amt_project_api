package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.EventsApi;
import ch.heigvd.amt.api.model.Event;
import ch.heigvd.amt.entities.UserEntity;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

public class EventsProcessorService implements EventsApi {

    @Override
    public ResponseEntity<Void> eventProcess(@Valid Event event) {
        return null;
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
