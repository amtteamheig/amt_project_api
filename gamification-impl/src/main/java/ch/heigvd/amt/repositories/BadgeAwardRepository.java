package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.ApiKeyEntity;
import ch.heigvd.amt.entities.DTOs.AwardAmountDTO;
import ch.heigvd.amt.entities.UserEntity;
import ch.heigvd.amt.entities.awards.BadgeAwardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BadgeAwardRepository extends JpaRepository<BadgeAwardEntity, Long> {
    Optional<List<BadgeAwardEntity>> findByUser_AndApiKeyEntity(UserEntity user, ApiKeyEntity apiKey);

    @Query(value = "SELECT ae.path AS path, COUNT(*) AS amount FROM award_entity AS ae " +
            "INNER JOIN user ON (user.id,user.fk_apikey) = (ae.fk_user,ae.fk_apikey) " +
            "WHERE user.id = ?1 " +
            "AND dtype = 'BadgeAwardEntity' " +
            "GROUP BY ae.path",
            nativeQuery = true)
    List<AwardAmountDTO> getAmounts(String userId);
}
