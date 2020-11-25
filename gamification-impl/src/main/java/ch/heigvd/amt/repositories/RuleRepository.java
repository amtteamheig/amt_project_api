package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.RuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends JpaRepository<RuleEntity, Long> {

}
