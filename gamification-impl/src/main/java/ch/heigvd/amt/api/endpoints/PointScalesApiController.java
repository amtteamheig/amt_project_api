package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.PointScalesApi;
import ch.heigvd.amt.entities.PointScaleEntity;
import ch.heigvd.amt.api.model.PointScale;
import ch.heigvd.amt.repositories.PointScaleRepository;
import io.swagger.annotations.ApiParam;
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
import java.util.ArrayList;
import java.util.List;

@Controller
public class PointScalesApiController implements PointScalesApi {

    @Autowired
    PointScaleRepository pointScaleRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createPointScale(@ApiParam(value = "", required = true) @Valid @RequestBody PointScale pointScale) {
        PointScaleEntity newPointScaleEntity = toPointScaleEntity(pointScale);
        pointScaleRepository.save(newPointScaleEntity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newPointScaleEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    public ResponseEntity<List<PointScale>> getPointScales() {
        List<PointScale> pointScales = new ArrayList<>();
        for (PointScaleEntity pointScaleEntity : pointScaleRepository.findAll()) {
            pointScales.add(toPointScale(pointScaleEntity));
        }
        return ResponseEntity.ok(pointScales);
    }



    @Override
    public ResponseEntity<PointScale> getPointScale(@ApiParam(value = "",required=true) @PathVariable("id") Integer id) {
        PointScaleEntity existingPointScaleEntity = pointScaleRepository.findById(Long.valueOf(id)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(toPointScale(existingPointScaleEntity));
    }

    private PointScaleEntity toPointScaleEntity(PointScale pointScale) {
        PointScaleEntity entity = new PointScaleEntity();
        entity.setKind(pointScale.getKind());
        entity.setPoints(pointScale.getPoints());
        return entity;
    }

    private PointScale toPointScale(PointScaleEntity entity) {
        PointScale pointScale = new PointScale();
        pointScale.setKind(entity.getKind());
        pointScale.setPoints(entity.getPoints());
        return pointScale;
    }

}
