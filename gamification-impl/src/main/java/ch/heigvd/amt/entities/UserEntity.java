package ch.heigvd.amt.entities;

import ch.heigvd.amt.api.model.Badge;
import ch.heigvd.amt.api.model.PointScale;
import ch.heigvd.amt.api.model.UserPoints;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class UserEntity {

    @Id
    private String id;

    //TODO placeholder for now, to show basic principle

    /*
    @OneToMany(mappedBy = "user")
    private List<BadgeEntity> badges = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserPointsEntity> points = new ArrayList<>();

    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Data
    public static class UserPointsEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @ManyToOne(mappedBy = "user_points")
        private PointScaleEntity pointScale;

        private Integer amount;
    }
    */
}
