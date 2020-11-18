package ch.heigvd.amt.entities;

import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
public class BadgeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String kind;
    private JsonNullable<String> imageUrl;

    @Column(columnDefinition = "DATE")
    private LocalDate obtainedDate;


}
