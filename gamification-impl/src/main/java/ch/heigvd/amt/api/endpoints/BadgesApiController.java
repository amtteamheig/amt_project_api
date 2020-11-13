package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.BadgesApi;
import ch.heigvd.amt.entities.BadgeEntity;
import ch.heigvd.amt.api.model.Badge;
import ch.heigvd.amt.repositories.BadgeRepository;
import io.swagger.annotations.ApiParam;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BadgesApiController implements BadgesApi {

    @Autowired
    BadgeRepository badgeRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createBadge(@ApiParam(value = "", required = true) @Valid @RequestBody Badge badge) {
        BadgeEntity newBadgeEntity = toBadgeEntity(badge);
        badgeRepository.save(newBadgeEntity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newBadgeEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    public ResponseEntity<List<Badge>> getBadges() {
        List<Badge> badges = new ArrayList<>();
        for (BadgeEntity badgeEntity : badgeRepository.findAll()) {
            badges.add(toBadge(badgeEntity));
        }
        return ResponseEntity.ok(badges);
    }

    @Override
    public ResponseEntity<Badge> getBadge(@ApiParam(value = "",required=true) @PathVariable("id") Integer id) {
        BadgeEntity existingBadgeEntity = badgeRepository.findById(Long.valueOf(id)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(toBadge(existingBadgeEntity));
    }

    private BadgeEntity toBadgeEntity(Badge badge) {
        BadgeEntity entity = new BadgeEntity();
        entity.setKind(badge.getKind());
        entity.setObtainedDate(badge.getObtainedDate());
        entity.setImageUrl(badge.getImageUrl());
        return entity;
    }

    private Badge toBadge(BadgeEntity entity) {
        Badge badge = new Badge();
        badge.setKind(entity.getKind());
        badge.setObtainedDate(entity.getObtainedDate());
        badge.setImageUrl(entity.getImageUrl());
        return badge;
    }

}
