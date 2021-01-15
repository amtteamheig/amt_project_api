package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.LeaderboardsApi;

import ch.heigvd.amt.api.model.PointScaleAward;
import ch.heigvd.amt.api.model.PointScaleLeaderboard;
import ch.heigvd.amt.api.model.PointScaleLeaderboardLeaderboard;
import ch.heigvd.amt.api.model.User;
import ch.heigvd.amt.entities.ApiKeyEntity;
import ch.heigvd.amt.entities.DTOs.LeaderboardDTO;
import ch.heigvd.amt.entities.PointScaleEntity;
import ch.heigvd.amt.entities.UserEntity;
import ch.heigvd.amt.repositories.ApiKeyRepository;
import ch.heigvd.amt.repositories.PointScaleAwardRepository;
import ch.heigvd.amt.repositories.PointScaleRepository;
import ch.heigvd.amt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public ResponseEntity<List<PointScaleLeaderboard>> getPointScalesLeaderboards(Integer limit) {
        return null;
    }

    @Override
    public ResponseEntity<PointScaleLeaderboard> getPointScaleLeaderboard(Integer id, Integer limit) {
        String apiKeyId = (String) servletRequest.getAttribute("Application");

        ApiKeyEntity apiKey = apiKeyRepository.findById(apiKeyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        PointScaleEntity pointScaleEntity = pointScaleRepository.findByApiKeyEntityValue_AndId(apiKey.getValue(), (long) id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no pointScale with that ID"));

        List<LeaderboardDTO> leaderBoards = pointScaleAwardRepository.getLeaderBoard(id,limit);

        List<PointScaleLeaderboardLeaderboard> users = new ArrayList<>();

        for(LeaderboardDTO leaderboard : leaderBoards){
            PointScaleLeaderboardLeaderboard psll = new PointScaleLeaderboardLeaderboard();
            psll.setUserId(leaderboard.getUserID());
            psll.setTotalPoints(leaderboard.getPoints());
            users.add(psll);
        }

        PointScaleLeaderboard psl = new PointScaleLeaderboard();
        psl.setName(pointScaleEntity.getName());
        psl.setLeaderboard(users);

        return ResponseEntity.ok(psl);
    }
}
