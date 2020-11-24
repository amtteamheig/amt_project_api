package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.ApiKeyEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ApiKeyRepository extends CrudRepository<ApiKeyEntity,  Long> {
    Optional<ApiKeyEntity> findByValue(String value);
}
