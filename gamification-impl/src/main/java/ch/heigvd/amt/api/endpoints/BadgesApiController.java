package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.BadgesApi;
import ch.heigvd.amt.api.model.JsonPatchDocument;
import ch.heigvd.amt.api.model.Link;
import ch.heigvd.amt.entities.ApiKeyEntity;
import ch.heigvd.amt.entities.BadgeEntity;
import ch.heigvd.amt.api.model.Badge;
import ch.heigvd.amt.repositories.ApiKeyRepository;
import ch.heigvd.amt.repositories.BadgeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.json.*;
import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@RestController
public class BadgesApiController implements BadgesApi {

    @Autowired
    BadgeRepository badgeRepository;

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Autowired
    ServletRequest servletRequest;

    @Autowired
    ObjectMapper objectMapper;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createBadge(@Valid Badge badge) {

        String apiKeyId = (String) servletRequest.getAttribute("Application");

        ApiKeyEntity apiKey = apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        BadgeEntity newBadgeEntity = toBadgeEntity(badge);
        newBadgeEntity.setApiKeyEntity(apiKey);
        badgeRepository.save(newBadgeEntity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newBadgeEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<List<Badge>> getBadges() {
        try {
            String apiKeyId = (String) servletRequest.getAttribute("Application");

            Optional<List<BadgeEntity>> badgeEntities = badgeRepository.findByApiKeyEntityValue(apiKeyId);

            List<Badge> badges = new ArrayList<>();

            if (badgeEntities.isPresent()) {
                for (BadgeEntity badgeEntity : badgeEntities.get()) {
                    badges.add(toBadge(badgeEntity));
                }
            }

            return ResponseEntity.ok(badges);
        } catch (URISyntaxException e) { // If the URI is not valid
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Badge> getBadge(Integer id) {
        try {
            String apiKeyId = (String) servletRequest.getAttribute("Application");

            BadgeEntity existingBadgeEntity =
                    badgeRepository.findByApiKeyEntityValue_AndId(apiKeyId, Long.valueOf(id))
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            return ResponseEntity.ok(toBadge(existingBadgeEntity));
        } catch (URISyntaxException e) { // If the URI is not valid
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Override
    public ResponseEntity<Void> patchBadge(Integer id, @Valid List<JsonPatchDocument> jsonPatchDocument) {

        try {


            String apiKeyId = (String) servletRequest.getAttribute("Application");

            BadgeEntity existingBadgeEntity =
                    badgeRepository.findByApiKeyEntityValue_AndId(apiKeyId, Long.valueOf(id))
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            List<JsonPatch> jsonPatches = toJsonPatch(jsonPatchDocument);

            for (JsonPatch jsonPatch : jsonPatches) {

                BadgeEntity patched = patch(jsonPatch, existingBadgeEntity, BadgeEntity.class);
                badgeRepository.save(patched);
            }
            return ResponseEntity.ok().build();

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }

    /**
     * Converts a badge to a badge entity
     *
     * @param badge : badge
     * @return badge entity
     */
    private BadgeEntity toBadgeEntity(Badge badge) {
        BadgeEntity entity = new BadgeEntity();
        entity.setName(badge.getName());
        entity.setObtainedDate(badge.getObtainedDate());
        entity.setImageUrl(badge.getImageUrl());
        return entity;
    }

    /**
     * Converts a badge entity to a badge
     *
     * @param entity : badge entity
     * @return badge
     */
    private Badge toBadge(BadgeEntity entity) throws URISyntaxException {
        Badge badge = new Badge();
        badge.setName(entity.getName());
        badge.setObtainedDate(entity.getObtainedDate());
        badge.setImageUrl(entity.getImageUrl());
        Link self = new Link();
        self.self(new URI(servletRequest.getLocalAddr() + "/badges/" + entity.getId()));
        badge.setLinks(Collections.singletonList(self));
        return badge;
    }

    private <T> T patch(JsonPatch patch, T targetBadge, Class<T> clazz) {

        JsonStructure target = objectMapper.convertValue(targetBadge, JsonStructure.class);

        JsonValue patchedValue = patch.apply(target);

        return objectMapper.convertValue(patchedValue, clazz);

    }

    private List<JsonPatch> toJsonPatch(List<JsonPatchDocument> jsonPatchDocuments) throws IOException {
        List<JsonPatch> jsonPatches = new ArrayList<>();


        for (JsonPatchDocument doc : jsonPatchDocuments) {
            JsonPatchBuilder builder = Json.createPatchBuilder();
            switch (doc.getOp()) {
                case REPLACE:
                    builder.replace(doc.getPath(), doc.getValue().toString());
                    break;
            }

            jsonPatches.add(builder.build());
        }

        return jsonPatches;
    }
}
