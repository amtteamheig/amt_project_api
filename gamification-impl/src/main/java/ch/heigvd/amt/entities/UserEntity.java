package ch.heigvd.amt.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "User")
public class UserEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "fk_apikey")
    private ApiKeyEntity apiKeyEntity;

}
