package ch.heigvd.amt.entities;

import com.sun.istack.Nullable;
import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;

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
