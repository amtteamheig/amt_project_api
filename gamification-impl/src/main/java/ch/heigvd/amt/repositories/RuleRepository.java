package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.RuleEntity;
import ch.heigvd.amt.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RuleRepository extends JpaRepository<RuleEntity, Long> {
    Optional<List<RuleEntity>> findByApiKeyEntityValue(String apiKeyId);
    Optional<RuleEntity> findByApiKeyEntityValue_AndId(String apiKeyId, Long id);
    Optional<RuleEntity> findBy_if_Type(String type);
}
