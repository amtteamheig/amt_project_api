package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.BadgeEntity;
import ch.heigvd.amt.entities.DTOs.LeaderboardDTO;
import ch.heigvd.amt.entities.UserEntity;
import ch.heigvd.amt.entities.awards.BadgeAwardEntity;
import ch.heigvd.amt.entities.awards.PointScaleAwardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PointScaleAwardRepository extends JpaRepository<PointScaleAwardEntity, Long> {
    Optional<List<PointScaleAwardEntity>> findByUser(UserEntity user);
/*
    @Query(value = "SELECT User, SUM(ps.amount) as totalPoints FROM PointScaleAward ps " +
            "INNER JOIN User ON User.id = ps.fk_user " +
            "WHERE User.fk_apikey = ?1 " +
            "AND ps.id = ?2 " +
            "ORDER BY totalPoints DESC " +
            "LIMIT ?3",
            nativeQuery = true)
    List<Object[]> getLeaderBoard(String apiKey, Integer id, Integer limit);*/

    @Query(value = "SELECT user.id as userID, SUM(ae.amount) AS points FROM award_entity AS ae " +
            "INNER JOIN user ON (user.id,user.fk_apikey) = (ae.fk_user,ae.fk_apikey) " +
            "WHERE ae.path = CONCAT('/pointScales/',?1) " +
            "GROUP BY user.id " +
            "ORDER BY points DESC " +
            "LIMIT ?2",
            nativeQuery = true)
    List<LeaderboardDTO> getLeaderBoard(Integer id, Integer limit);
}
