package ch.heigvd.amt.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    //TODO

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
