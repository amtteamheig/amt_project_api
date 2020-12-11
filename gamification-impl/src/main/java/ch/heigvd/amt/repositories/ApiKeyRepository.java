package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.ApiKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity,  String> {

}
