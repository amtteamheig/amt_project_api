package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.ApiKeyEntity;
import ch.heigvd.amt.entities.UserEntity;
import ch.heigvd.amt.entities.awards.BadgeAwardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BadgeAwardRepository extends JpaRepository<BadgeAwardEntity, Long> {
    Optional<List<BadgeAwardEntity>> findByUser_AndApiKeyEntity(UserEntity user, ApiKeyEntity apiKey);
}
