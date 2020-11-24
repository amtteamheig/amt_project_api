package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.ApiKeyEntity;
import ch.heigvd.amt.entities.BadgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface BadgeRepository extends JpaRepository<BadgeEntity, Long> {
    Optional<List<BadgeEntity>> findByApiKeyEntityId(long id);

    Optional<BadgeEntity> findByApiKeyEntityId_AndId(long apiKeyId, long badgeId);


}
