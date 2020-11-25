package ch.heigvd.amt.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class UserEntity {

    @Id
    private String id;

    @OneToMany(mappedBy = "user")
    private Set<BadgeEntity> badges;

    @OneToMany(mappedBy = "user")
    private Set<PointScaleEntity> pointScales;
}
