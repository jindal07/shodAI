package com.shodhai.contest.controller;

import com.shodhai.contest.dto.request.JoinContestRequest;
import com.shodhai.contest.dto.response.ApiResponse;
import com.shodhai.contest.dto.response.ContestResponse;
import com.shodhai.contest.dto.response.ProblemDetailResponse;
import com.shodhai.contest.model.User;
import com.shodhai.contest.service.ContestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/contests")
@RequiredArgsConstructor
@Slf4j
public class ContestController {
    
    private final ContestService contestService;
    
    @GetMapping("/{contestId}")
    public ResponseEntity<ApiResponse<ContestResponse>> getContest(@PathVariable Long contestId) {
        log.info("GET /api/contests/{}", contestId);
        ContestResponse contest = contestService.getContestById(contestId);
        return ResponseEntity.ok(ApiResponse.success(contest));
    }
    
    @PostMapping("/{contestId}/join")
    public ResponseEntity<ApiResponse<Map<String, Object>>> joinContest(
            @PathVariable Long contestId,
            @Valid @RequestBody JoinContestRequest request) {
        log.info("POST /api/contests/{}/join - User: {}", contestId, request.getUsername());
        User user = contestService.joinContest(contestId, request);
        
        Map<String, Object> response = Map.of(
            "userId", user.getId(),
            "username", user.getUsername(),
            "message", "Successfully joined contest"
        );
        
        return ResponseEntity.ok(ApiResponse.success("Successfully joined contest", response));
    }
    
    @GetMapping("/{contestId}/leaderboard")
    public ResponseEntity<ApiResponse<com.shodhai.contest.dto.response.LeaderboardResponse>> getLeaderboard(
            @PathVariable Long contestId) {
        log.info("GET /api/contests/{}/leaderboard", contestId);
        com.shodhai.contest.dto.response.LeaderboardResponse leaderboard = 
                contestService.getContestById(contestId) != null 
                ? getLeaderboardData(contestId) 
                : null;
        return ResponseEntity.ok(ApiResponse.success(leaderboard));
    }
    
    @GetMapping("/{contestId}/problems/{problemId}")
    public ResponseEntity<ApiResponse<ProblemDetailResponse>> getProblem(
            @PathVariable Long contestId,
            @PathVariable Long problemId) {
        log.info("GET /api/contests/{}/problems/{}", contestId, problemId);
        ProblemDetailResponse problem = contestService.getProblemById(problemId);
        return ResponseEntity.ok(ApiResponse.success(problem));
    }
    
    private com.shodhai.contest.dto.response.LeaderboardResponse getLeaderboardData(Long contestId) {
        // This will be delegated to LeaderboardController
        return null;
    }
}

