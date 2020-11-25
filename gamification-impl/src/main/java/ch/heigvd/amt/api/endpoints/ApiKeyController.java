package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.RegistrationApi;
import ch.heigvd.amt.entities.ApiKeyEntity;
import ch.heigvd.amt.api.model.ApiKey;
import ch.heigvd.amt.repositories.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ApiKeyController implements RegistrationApi {

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Override
    public ResponseEntity<ApiKey> registerApplication() {

        ApiKey apiKey = new ApiKey();
        apiKey.setValue(UUID.randomUUID());

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
