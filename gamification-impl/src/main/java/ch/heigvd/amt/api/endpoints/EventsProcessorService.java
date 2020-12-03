package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.EventsApi;
import ch.heigvd.amt.api.model.Event;
import ch.heigvd.amt.entities.ApiKeyEntity;
import ch.heigvd.amt.entities.UserEntity;
import ch.heigvd.amt.repositories.ApiKeyRepository;
import ch.heigvd.amt.repositories.UserRepository;
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

@RestController
public class EventsProcessorService implements EventsApi {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Autowired
    ServletRequest servletRequest;

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> eventProcess(@Valid Event event) {

        String apiKeyId = (String) servletRequest.getAttribute("Application");

        ApiKeyEntity apiKey = apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        UserEntity user = UsersController.toUserEntity(event.getUserId());
        user.setApiKeyEntity(apiKey);
        userRepository.save(user);

        //TODO handle rules
        
        return ResponseEntity.ok().build();
    }

}
