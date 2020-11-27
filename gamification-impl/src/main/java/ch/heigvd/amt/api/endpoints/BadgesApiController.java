package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.BadgesApi;
import ch.heigvd.amt.entities.ApiKeyEntity;
import ch.heigvd.amt.entities.BadgeEntity;
import ch.heigvd.amt.api.model.Badge;
import ch.heigvd.amt.repositories.ApiKeyRepository;
import ch.heigvd.amt.repositories.BadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
public class BadgesApiController implements BadgesApi {

    @Autowired
    BadgeRepository badgeRepository;

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Autowired
    ServletRequest servletRequest;

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
        String apiKeyId = (String) servletRequest.getAttribute("Application");

        Optional<List<BadgeEntity>> badgeEntities = badgeRepository.findByApiKeyEntityValue(apiKeyId);

        List<Badge> badges = new ArrayList<>();

        if (badgeEntities.isPresent()) {
            for (BadgeEntity badgeEntity : badgeEntities.get()) {
                badges.add(toBadge(badgeEntity));
            }
        }

        return ResponseEntity.ok(badges);
    }

    @Override
    public ResponseEntity<Badge> getBadge(Integer id) {
        String apiKeyId = (String) servletRequest.getAttribute("Application");

        BadgeEntity existingBadgeEntity =
                badgeRepository.findByApiKeyEntityValue_AndId(apiKeyId, Long.valueOf(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(toBadge(existingBadgeEntity));
    }

    /**
     * Converts a badge to a badge entity
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

}
