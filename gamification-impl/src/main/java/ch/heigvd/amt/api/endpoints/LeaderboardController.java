package ch.heigvd.amt.api.endpoints;

import ch.heigvd.amt.api.LeaderboardsApi;

import ch.heigvd.amt.api.model.PointScaleLeaderboard;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LeaderboardController implements LeaderboardsApi {
    @Override
    public ResponseEntity<List<PointScaleLeaderboard>> getLeaderboards(Integer limit) {
        return null;
    }

    @Override
    public ResponseEntity<PointScaleLeaderboard> getPointScaleLeaderboard(Integer id, Integer limit) {
        return null;
    }
}
