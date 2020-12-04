package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.PointScalesApi;
import ch.heigvd.amt.api.model.JsonPatchDocument;
import ch.heigvd.amt.api.model.Link;
import ch.heigvd.amt.entities.ApiKeyEntity;
import ch.heigvd.amt.entities.BadgeEntity;
import ch.heigvd.amt.entities.PointScaleEntity;
import ch.heigvd.amt.api.model.PointScale;
import ch.heigvd.amt.repositories.ApiKeyRepository;
import ch.heigvd.amt.repositories.PointScaleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ch.heigvd.amt.api.util.Patch.toJsonPatch;

@RestController
public class PointScalesApiController implements PointScalesApi {

    @Autowired
    PointScaleRepository pointScaleRepository;

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Autowired
    ServletRequest servletRequest;

    @Autowired
    ObjectMapper objectMapper;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createPointScale(@Valid PointScale pointScale) {
        String apiKeyId = (String) servletRequest.getAttribute("Application");

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
        try {
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
        } catch (URISyntaxException e) {// If the URI is not valid
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Override
    public ResponseEntity<PointScale> getPointScale(Integer id) {
        try {
            String apiKeyId = (String) servletRequest.getAttribute("Application");

            PointScaleEntity existingPointScaleEntity =
                    pointScaleRepository.findByApiKeyEntityValue_AndId(apiKeyId, Long.valueOf(id))
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            return ResponseEntity.ok(toPointScale(existingPointScaleEntity));
        } catch (URISyntaxException e) {// If the URI is not valid
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> patchPointScale(Integer id, @Valid List<JsonPatchDocument> jsonPatchDocument) {
        String apiKeyId = (String) servletRequest.getAttribute("Application");

        PointScaleEntity existingPointScale =
                pointScaleRepository.findByApiKeyEntityValue_AndId(apiKeyId, Long.valueOf(id))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED));


        List<JsonPatch> jsonPatches = toJsonPatch(jsonPatchDocument);


        for (JsonPatch jsonPatch : jsonPatches) {
            PointScaleEntity patched = patch(jsonPatch, existingPointScale);
            // If the entity already exists, save() will update it
            pointScaleRepository.save(patched);
        }
        return ResponseEntity.ok().build();

    }

    private PointScaleEntity toPointScaleEntity(PointScale pointScale) {
        PointScaleEntity entity = new PointScaleEntity();
        entity.setName(pointScale.getName());
        entity.setDescription(pointScale.getDescription());
        return entity;
    }

    private PointScale toPointScale(PointScaleEntity entity) throws URISyntaxException {
        PointScale pointScale = new PointScale();
        pointScale.setName(entity.getName());
        pointScale.setDescription(entity.getDescription());
        Link self = new Link();
        self.self(new URI(servletRequest.getLocalAddr() + "/pointScales/" + entity.getId()));
        pointScale.setLinks(Collections.singletonList(self));
        return pointScale;
    }

    private PointScaleEntity patch(JsonPatch patch, PointScaleEntity targetPointScale) {

        JsonStructure target = objectMapper.convertValue(targetPointScale, JsonStructure.class);

        JsonValue patchedValue = patch.apply(target);

        return objectMapper.convertValue(patchedValue, PointScaleEntity.class);

    }

}
