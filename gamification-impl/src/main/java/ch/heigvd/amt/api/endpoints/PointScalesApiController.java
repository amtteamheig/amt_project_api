package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.PointScalesApi;
import ch.heigvd.amt.api.exceptions.ApiException;
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

@RestController
public class PointScalesApiController implements PointScalesApi {

    @Autowired
    PointScaleRepository pointScaleRepository;

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Autowired
    ServletRequest servletRequest;

    /**
     * Servlet entry point POST /pointScales
     * @param pointScale (optional) pointScale object built by user
     * @return response
     */
    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createPointScale(@Valid PointScale pointScale) {
        String apiKeyId = (String) servletRequest.getAttribute("Application");

        ApiKeyEntity apiKey = apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        try {
            PointScaleEntity newPointScaleEntity = toPointScaleEntity(pointScale);
            newPointScaleEntity.setApiKeyEntity(apiKey);
            pointScaleRepository.save(newPointScaleEntity);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(newPointScaleEntity.getId()).toUri();

            return ResponseEntity.created(location).build();
        } catch(ApiException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
        }
    }

    /**
     * Servlet entry point GET /pointScales
     * @return response
     */
    @Override
    public ResponseEntity<List<PointScale>> getPointScales() {

        String apiKeyId = (String) servletRequest.getAttribute("Application");
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

    /**
     * Servlet entry point GET /pointScale/id
     * @param id pointScale's id
     * @return response
     */
    @Override
    public ResponseEntity<PointScale> getPointScale(Integer id) {

        String apiKeyId = (String) servletRequest.getAttribute("Application");

        PointScaleEntity existingPointScaleEntity =
                pointScaleRepository.findByApiKeyEntityValue_AndId(apiKeyId, Long.valueOf(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(toPointScale(existingPointScaleEntity));
    }

    /**
     * convert pointScale to pointScaleEntity
     * @param pointScale pointScale
     * @return pointScaleEntity
     */
    private PointScaleEntity toPointScaleEntity(PointScale pointScale) throws ApiException {
        PointScaleEntity entity = new PointScaleEntity();

        if(pointScale.getName().isEmpty()) {
           throw new ApiException(400, "Name is empty");
        }

        if(pointScale.getDescription().isEmpty()) {
            throw new ApiException(400, "Description is empty");
        }

        entity.setName(pointScale.getName());
        entity.setDescription(pointScale.getDescription());
        return entity;
    }

    /**
     * convert pointScaleEntity to pointScale
     * @param entity pointScaleEntity
     * @return pointScale
     */
    private PointScale toPointScale(PointScaleEntity entity) {
        PointScale pointScale = new PointScale();
        pointScale.setName(entity.getName());
        pointScale.setDescription(entity.getDescription());
        return pointScale;
    }

}
