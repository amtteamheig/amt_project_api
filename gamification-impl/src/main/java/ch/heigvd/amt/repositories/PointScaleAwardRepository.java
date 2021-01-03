package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.BadgeEntity;
import ch.heigvd.amt.entities.UserEntity;
import ch.heigvd.amt.entities.awards.BadgeAwardEntity;
import ch.heigvd.amt.entities.awards.PointScaleAwardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PointScaleAwardRepository extends JpaRepository<PointScaleAwardEntity, Long> {
    Optional<List<PointScaleAwardEntity>> findByUser(UserEntity user);

    @Query(value = "SELECT User, SUM(ps.amount) as totalPoints FROM PointScaleAward ps " +
            "INNER JOIN User ON User.id = ps.fk_user " +
            "WHERE User.fk_apikey = ?1 " +
            "AND ps.id = ?2 " +
            "ORDER BY totalPoints DESC " +
            "LIMIT ?3",
            nativeQuery = true)
    List<Object[]> getLeaderBoard(String apiKey, Integer id, Integer limit);

    /*
    @Query(value = "SELECT User, SUM(ps.amount) as totalPoints FROM PointScaleAward ps " +
                    "INNER JOIN User ON User.id = ps.fk_user " +
                    "WHERE User.fk_apikey = ?1 " +
                    "AND ps.id = ?2 " +
                    "ORDER BY totalPoints DESC")
    List<Object[]> getLeaderBoard(String apiKey, Integer id, Integer limit);*/
}
