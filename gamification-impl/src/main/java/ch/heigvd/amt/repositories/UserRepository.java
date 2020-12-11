package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<List<UserEntity>> findByApiKeyEntityValue(String apiKeyId);
    Optional<UserEntity> findByApiKeyEntityValue_AndId(String apiKeyId, String UserId);

    @Query(value = "SELECT * FROM User WHERE User.fk_apikey = ?1", nativeQuery = true)
    Optional<List<UserEntity>> getPointScaleLeaderBoard(String apiKeyId, Integer id, Integer limit);
}
