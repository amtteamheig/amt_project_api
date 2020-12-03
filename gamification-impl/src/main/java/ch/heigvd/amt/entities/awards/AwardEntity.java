package ch.heigvd.amt.entities.awards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public abstract class AwardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String reason;
    private OffsetDateTime timestamp;
    private String path;

}
