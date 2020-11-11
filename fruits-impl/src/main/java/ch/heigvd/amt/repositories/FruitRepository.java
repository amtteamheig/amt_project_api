package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.FruitEntity;
import org.springframework.data.repository.CrudRepository;

public interface FruitRepository extends CrudRepository<FruitEntity, Long> {

}
