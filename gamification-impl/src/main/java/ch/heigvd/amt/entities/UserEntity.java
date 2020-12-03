package ch.heigvd.amt.entities;

import ch.heigvd.amt.entities.awards.BadgeAwardEntity;
import ch.heigvd.amt.entities.awards.PointScaleAwardEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class UserEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "fk_apikey")
    private ApiKeyEntity apiKeyEntity;

    @OneToMany
    private List<BadgeAwardEntity> badgesAwards = new ArrayList<>();

    @OneToMany
    private List<PointScaleAwardEntity> pointsAwards = new ArrayList<>();


}
