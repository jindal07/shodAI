package com.shodhai.contest.controller;

import com.shodhai.contest.dto.response.ApiResponse;
import com.shodhai.contest.dto.response.LeaderboardResponse;
import com.shodhai.contest.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LeaderboardController {
    
    private final LeaderboardService leaderboardService;
    
    @GetMapping("/contests/{contestId}/leaderboard")
    public ResponseEntity<ApiResponse<LeaderboardResponse>> getLeaderboard(
            @PathVariable Long contestId) {
        log.info("GET /api/contests/{}/leaderboard", contestId);
        
        LeaderboardResponse leaderboard = leaderboardService.getLeaderboard(contestId);
        
        return ResponseEntity.ok(ApiResponse.success(leaderboard));
    }
}

