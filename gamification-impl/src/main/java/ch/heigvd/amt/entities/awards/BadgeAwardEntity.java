package ch.heigvd.amt.entities.awards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Entity
@Data
public class BadgeAwardEntity extends AwardEntity {

}
