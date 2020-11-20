package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.ApiKeyEntity;
import org.springframework.data.repository.CrudRepository;

public interface ApiKeyRepository extends CrudRepository<ApiKeyEntity,  Long> {

}
