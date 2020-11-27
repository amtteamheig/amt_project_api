package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.PointScalesApi;
import ch.heigvd.amt.entities.ApiKeyEntity;
import ch.heigvd.amt.entities.PointScaleEntity;
import ch.heigvd.amt.api.model.PointScale;
import ch.heigvd.amt.repositories.ApiKeyRepository;
import ch.heigvd.amt.repositories.PointScaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class PointScalesApiController implements PointScalesApi {

    @Autowired
    PointScaleRepository pointScaleRepository;

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Autowired
    ServletRequest servletRequest;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createPointScale(@Valid PointScale pointScale) {
        UUID apiKeyId = (UUID) servletRequest.getAttribute("Application");

        ApiKeyEntity apiKey = apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        PointScaleEntity newPointScaleEntity = toPointScaleEntity(pointScale);
        newPointScaleEntity.setApiKeyEntity(apiKey);
        pointScaleRepository.save(newPointScaleEntity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newPointScaleEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<List<PointScale>> getPointScales() {

        UUID apiKeyId = (UUID) servletRequest.getAttribute("Application");

        Optional<List<PointScaleEntity>> pointScalesEntries =
                pointScaleRepository.findByApiKeyEntityValue(apiKeyId);

        List<PointScale> pointScales = new ArrayList<>();

        if (pointScalesEntries.isPresent()) {
            for (PointScaleEntity pointScaleEntity : pointScalesEntries.get()) {
                pointScales.add(toPointScale(pointScaleEntity));
            }
        }
        return ResponseEntity.ok(pointScales);
    }


    @Override
    public ResponseEntity<PointScale> getPointScale(Integer id) {

        UUID apiKeyId = (UUID) servletRequest.getAttribute("Application");

        PointScaleEntity existingPointScaleEntity =
                pointScaleRepository.findByApiKeyEntityValue_AndId(apiKeyId, Long.valueOf(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(toPointScale(existingPointScaleEntity));
    }

    private PointScaleEntity toPointScaleEntity(PointScale pointScale) {
        PointScaleEntity entity = new PointScaleEntity();
        entity.setName(pointScale.getName());
        entity.setDescription(pointScale.getDescription());
        return entity;
    }

    private PointScale toPointScale(PointScaleEntity entity) {
        PointScale pointScale = new PointScale();
        pointScale.setName(entity.getName());
        pointScale.setDescription(entity.getDescription());
        return pointScale;
    }

}
