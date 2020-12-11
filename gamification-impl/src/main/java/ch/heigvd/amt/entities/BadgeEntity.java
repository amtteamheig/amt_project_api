package ch.heigvd.amt.entities;

import ch.heigvd.amt.api.model.ApiKey;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.data.annotation.Reference;
import springfox.documentation.spring.web.json.Json;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class BadgeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "fk_apikey")
    private ApiKeyEntity apiKeyEntity;

    private String name;
    private JsonNullable<String> imageUrl;

    @Column(columnDefinition = "DATE")
    private LocalDate obtainedDate;

}
