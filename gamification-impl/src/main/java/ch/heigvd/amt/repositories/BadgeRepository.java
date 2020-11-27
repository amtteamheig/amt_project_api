package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.ApiKeyEntity;
import ch.heigvd.amt.entities.BadgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BadgeRepository extends JpaRepository<BadgeEntity, Long> {
    Optional<List<BadgeEntity>> findByApiKeyEntityValue(String apiKeyId);
    Optional<BadgeEntity> findByApiKeyEntityValue_AndId(String apiKeyId, Long badgeId);
}
