package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.DTOs.PointScaleLeaderboardUserDTO;
import ch.heigvd.amt.entities.UserEntity;
import ch.heigvd.amt.entities.awards.PointScaleAwardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PointScaleAwardRepository extends JpaRepository<PointScaleAwardEntity, Long> {
    Optional<List<PointScaleAwardEntity>> findByUser(UserEntity user);

    @Query(value = "SELECT user.id as userID, SUM(ae.amount) AS points FROM award_entity AS ae " +
            "INNER JOIN user ON (user.id,user.fk_apikey) = (ae.fk_user,ae.fk_apikey) " +
            "WHERE ae.path = CONCAT('/pointScales/',?1) " +
            "GROUP BY user.id " +
            "ORDER BY points DESC " +
            "LIMIT ?2",
            nativeQuery = true)
    List<PointScaleLeaderboardUserDTO> getLeaderBoard(Integer id, Integer limit);
}
