package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.BadgeEntity;
import ch.heigvd.amt.entities.PointScaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Repository
public interface PointScaleRepository extends JpaRepository<PointScaleEntity, Long> {

    Optional<List<PointScaleEntity>> findByApiKeyEntityValue(String apiKeyId);
    Optional<PointScaleEntity> findByApiKeyEntityValue_AndId(String apiKeyId, Long pointScaleId);

}