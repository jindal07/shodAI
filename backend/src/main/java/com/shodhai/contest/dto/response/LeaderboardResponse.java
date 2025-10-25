package com.shodhai.contest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardResponse {
    private Long contestId;
    private LocalDateTime lastUpdated;
    private List<LeaderboardEntry> entries;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LeaderboardEntry {
        private Integer rank;
        private Long userId;
        private String username;
        private Integer totalScore;
        private Integer problemsSolved;
        private LocalDateTime lastSubmissionTime;
    }
}

