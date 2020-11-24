package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.BadgeEntity;
import ch.heigvd.amt.entities.PointScaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
@Repository
public interface PointScaleRepository extends JpaRepository<PointScaleEntity, Long> {

    Optional<List<PointScaleEntity>> findByApiKeyEntityId(long id);

    Optional<PointScaleEntity> findByApiKeyEntityId_AndId(long apiKeyId, Long valueOf);
}