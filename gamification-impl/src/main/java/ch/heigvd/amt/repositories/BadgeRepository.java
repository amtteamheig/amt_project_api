package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.ApiKeyEntity;
import ch.heigvd.amt.entities.BadgeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BadgeRepository extends CrudRepository<BadgeEntity, Long> {
    Optional<List<BadgeEntity>> findByApiKeyEntityValue(String value);

    Optional<BadgeEntity> findByApiKeyEntityValue_AndId(String value, long id);


}
