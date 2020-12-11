package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.LeaderboardsApi;

import ch.heigvd.amt.api.model.PointScaleLeaderboard;
import ch.heigvd.amt.entities.ApiKeyEntity;
import ch.heigvd.amt.repositories.ApiKeyRepository;
import ch.heigvd.amt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletRequest;
import java.util.List;

@RestController
public class LeaderboardController implements LeaderboardsApi {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Autowired
    ServletRequest servletRequest;

    @Override
    public ResponseEntity<List<PointScaleLeaderboard>> getLeaderboards(Integer limit) {
        return null;
    }

    @Override
    public ResponseEntity<PointScaleLeaderboard> getPointScaleLeaderboard(Integer id, Integer limit) {
        String apiKeyId = (String) servletRequest.getAttribute("Application");

        ApiKeyEntity apiKey = apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        userRepository.getPointScaleLeaderBoard(apiKeyId,id,limit);

        return ResponseEntity.ok().build();
    }
}
