package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.BadgeEntity;
import ch.heigvd.amt.entities.UserEntity;
import ch.heigvd.amt.entities.awards.BadgeAwardEntity;
import ch.heigvd.amt.entities.awards.PointScaleAwardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PointScaleAwardRepository extends JpaRepository<PointScaleAwardEntity, Long> {
    Optional<List<PointScaleAwardEntity>> findByUser(UserEntity user);
    //List<UserEntity> getLeaderBoard(String value, Integer id, Integer limit);
}
