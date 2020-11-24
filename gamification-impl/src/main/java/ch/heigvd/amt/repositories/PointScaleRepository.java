package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.BadgeEntity;
import ch.heigvd.amt.entities.PointScaleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PointScaleRepository extends CrudRepository<PointScaleEntity, Long> {

    Optional<List<PointScaleEntity>> findByApiKeyEntityId(long id);

    Optional<PointScaleEntity> findByApiKeyEntityId_AndId(long apiKeyId, Long valueOf);
}