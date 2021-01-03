package ch.heigvd.amt.entities.awards;

import ch.heigvd.amt.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public abstract class AwardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "fk_apikey")
    private UserEntity user;

    private String reason;
    private OffsetDateTime timestamp;
    private String path;

}
