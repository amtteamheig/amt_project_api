package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.BadgesApi;
import ch.heigvd.amt.api.model.BadgeResponse;
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
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static ch.heigvd.amt.api.util.Patch.toJsonPatch;

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

    /**
     * Servlet entry point POST /badges
     *
     * @param badge (optional) badge object built by user
     * @return response
     */
    @Override
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

    /**
     * Servlet entry point GET /badges
     *
     * @return response
     */
    @Override
    public ResponseEntity<List<BadgeResponse>> getBadges() {
        try {
            String apiKeyId = (String) servletRequest.getAttribute("Application");

            Optional<List<BadgeEntity>> badgeEntities = badgeRepository.findByApiKeyEntityValue(apiKeyId);

            List<BadgeResponse> badges = new ArrayList<>();

            if (badgeEntities.isPresent()) {
                for (BadgeEntity badgeEntity : badgeEntities.get()) {
                    badges.add(toBadgeResponse(badgeEntity));
                }
            }

            return ResponseEntity.ok(badges);
        } catch (URISyntaxException e) { // If the URI is not valid
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't create the link URI");
        }
    }

    @Override
    public ResponseEntity<BadgeResponse> getBadge(Integer id) {
        try {
            String apiKeyId = (String) servletRequest.getAttribute("Application");

            BadgeEntity existingBadgeEntity =
                    badgeRepository.findByApiKeyEntityValue_AndId(apiKeyId, Long.valueOf(id))
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Can't the badge entity in the repository"));

            return ResponseEntity.ok(toBadgeResponse(existingBadgeEntity));
        } catch (URISyntaxException e) { // If the URI is not valid
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't create the link URI");
        }
    }

    /**
     * Servlet entry point GET /badge/id
     *
     * @param id badge id
     * @return response
     */
    @Override
    public ResponseEntity<Void> patchBadge(Integer id, @Valid List<JsonPatchDocument> jsonPatchDocument) {
        try {

            String apiKeyId = (String) servletRequest.getAttribute("Application");

            BadgeEntity existingBadgeEntity =
                    badgeRepository.findByApiKeyEntityValue_AndId(apiKeyId, Long.valueOf(id))
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Can't the badge entity in the repository"));

            List<JsonPatch> jsonPatches = toJsonPatch(jsonPatchDocument);

            for (JsonPatch jsonPatch : jsonPatches) {

                BadgeEntity patched = patch(jsonPatch, existingBadgeEntity);
                // If the entity already exists, save() will update it
                badgeRepository.save(patched);
            }
            return ResponseEntity.ok().build();

        } catch (JsonException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The body is malformed");
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
    private Badge toBadge(BadgeEntity entity) {
        Badge badge = new Badge();
        badge.setName(entity.getName());
        badge.setObtainedDate(entity.getObtainedDate());
        badge.setImageUrl(entity.getImageUrl());
        return badge;
    }

    /**
     * Converts a badge entity to a badge response with links
     *
     * @param entity : badge entity
     * @return badge response
     * @throws URISyntaxException
     */
    private BadgeResponse toBadgeResponse(BadgeEntity entity) throws URISyntaxException {
        BadgeResponse badgeResponse = new BadgeResponse();
        badgeResponse.setName(entity.getName());
        badgeResponse.setObtainedDate(entity.getObtainedDate());
        badgeResponse.setImageUrl(entity.getImageUrl());
        Link self = new Link();
        String url = servletRequest.getScheme() + "://" + servletRequest.getServerName() + ":" +
                servletRequest.getServerPort();
        self.self(new URI(url + "/badges/" + entity.getId()));
        badgeResponse.setLinks(Collections.singletonList(self));
        return badgeResponse;
    }

    /**
     * Return a badge with the modification
     *
     * @param patch       Changes that need to be performed
     * @param targetBadge Where apply this changes
     * @return the patched entity
     */
    private BadgeEntity patch(JsonPatch patch, BadgeEntity targetBadge) throws JsonException {

        JsonStructure target = objectMapper.convertValue(targetBadge, JsonStructure.class);

        JsonValue patchedValue = patch.apply(target);

        return objectMapper.convertValue(patchedValue, BadgeEntity.class);

    }
}
