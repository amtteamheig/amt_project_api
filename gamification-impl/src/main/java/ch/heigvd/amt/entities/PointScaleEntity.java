package ch.heigvd.amt.entities;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class PointScaleEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String kind;
    private Integer points;

    @ManyToOne
    @JoinColumn(name = "fk_apikey", referencedColumnName = "id")
    private ApiKeyEntity apiKeyEntity;
}
