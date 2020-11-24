package ch.heigvd.amt.repositories;

import ch.heigvd.amt.entities.PointScaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointScaleRepository extends JpaRepository<PointScaleEntity, Long> {

}