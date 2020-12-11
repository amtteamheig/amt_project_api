package ch.heigvd.amt.entities.awards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Entity
@Data
@Table(name = "BadgeAward")
public class BadgeAwardEntity extends AwardEntity {

}
