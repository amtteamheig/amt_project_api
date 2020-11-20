package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.RegistrationApi;
import ch.heigvd.amt.entities.ApiKeyEntity;
import ch.heigvd.amt.api.model.ApiKey;
import ch.heigvd.amt.repositories.ApiKeyRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class ApiKeyController implements RegistrationApi {

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiKey> registerApplication() {

        ApiKey apiKey = new ApiKey();
        apiKey.setValue(UUID.randomUUID().toString());

        ApiKeyEntity newApiKeyEntity = toApiKeyEntity(apiKey);
        apiKeyRepository.save(newApiKeyEntity);

        return new ResponseEntity<>(apiKey, HttpStatus.CREATED);
    }

    private ApiKeyEntity toApiKeyEntity(ApiKey apiKey) {
        ApiKeyEntity entity = new ApiKeyEntity();
        entity.setValue(apiKey.getValue());
        return entity;
    }

}
