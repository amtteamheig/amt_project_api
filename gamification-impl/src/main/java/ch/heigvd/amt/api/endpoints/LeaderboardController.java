package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.LeaderboardsApi;

import ch.heigvd.amt.api.model.PointScaleLeaderboard;
import ch.heigvd.amt.api.model.PointScaleLeaderboardLeaderboard;
import ch.heigvd.amt.entities.ApiKeyEntity;
import ch.heigvd.amt.entities.DTOs.PointScaleLeaderboardUserDTO;
import ch.heigvd.amt.entities.PointScaleEntity;
import ch.heigvd.amt.repositories.ApiKeyRepository;
import ch.heigvd.amt.repositories.PointScaleAwardRepository;
import ch.heigvd.amt.repositories.PointScaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class LeaderboardController implements LeaderboardsApi {

    @Autowired
    PointScaleAwardRepository pointScaleAwardRepository;

    @Autowired
    PointScaleRepository pointScaleRepository;

    @Autowired
    ApiKeyRepository apiKeyRepository;

    @Autowired
    ServletRequest servletRequest;

    /**
     * get leaderboards for pointScales in current app
     * @param limit leaderboard max length
     * @return the leaderboards
     */
    @Override
    public ResponseEntity<List<PointScaleLeaderboard>> getPointScalesLeaderboards(Integer limit) {
        String apiKeyId = (String) servletRequest.getAttribute("Application");

        ApiKeyEntity apiKey = apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Optional<List<PointScaleEntity>> pointScalesEntities = pointScaleRepository.findByApiKeyEntityValue(apiKey.getValue());

        List<PointScaleLeaderboard> psls = new ArrayList<>();

        if(pointScalesEntities.isPresent()) {
            for (PointScaleEntity pointScaleEntity : pointScalesEntities.get()) {
                List<PointScaleLeaderboardUserDTO> leaderBoard = pointScaleAwardRepository.getLeaderBoard((int) pointScaleEntity.getId(), limit);
                psls.add(toPointScaleLeaderboard(pointScaleEntity, leaderBoard));
            }
        }

        return ResponseEntity.ok(psls);
    }

    /**
     * get leaderboard for a pointScale
     * @param limit leaderboard max length
     * @param id pointScale's id
     * @return the leaderboard
     */
    @Override
    public ResponseEntity<PointScaleLeaderboard> getPointScaleLeaderboard(Integer id, Integer limit) {
        String apiKeyId = (String) servletRequest.getAttribute("Application");

        ApiKeyEntity apiKey = apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        PointScaleEntity pointScaleEntity = pointScaleRepository.findByApiKeyEntityValue_AndId(apiKey.getValue(), (long) id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no pointScale with that ID"));

        List<PointScaleLeaderboardUserDTO> leaderBoard = pointScaleAwardRepository.getLeaderBoard(id,limit);

        PointScaleLeaderboard psl = toPointScaleLeaderboard(pointScaleEntity, leaderBoard);

        return ResponseEntity.ok(psl);
    }

    /**
     * Converts a leaderboardDTO to a PointScaleleaderboard
     * @param pointScaleEntity the pointScale of the leaderboard
     * @param leaderBoard the leaderboard
     * @return a pointScaleLeaderboard
     */
    private PointScaleLeaderboard toPointScaleLeaderboard(PointScaleEntity pointScaleEntity, List<PointScaleLeaderboardUserDTO> leaderBoard) {
        List<PointScaleLeaderboardLeaderboard> users = new ArrayList<>();

        for(PointScaleLeaderboardUserDTO leaderboardUser : leaderBoard){
            PointScaleLeaderboardLeaderboard psll = new PointScaleLeaderboardLeaderboard();
            psll.setUserId(leaderboardUser.getUserID());
            psll.setTotalPoints(leaderboardUser.getPoints());
            users.add(psll);
        }

        PointScaleLeaderboard psl = new PointScaleLeaderboard();
        psl.setName(pointScaleEntity.getName());
        psl.setLeaderboard(users);
        return psl;
    }
}
