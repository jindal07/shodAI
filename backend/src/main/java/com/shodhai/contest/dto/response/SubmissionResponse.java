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
public class SubmissionResponse {
    private Long id;
    private Long userId;
    private Long problemId;
    private Long contestId;
    private String language;
    private String status;
    private String result;
    private Integer score;
    private Integer executionTimeMs;
    private Integer memoryUsedMb;
    private String errorMessage;
    private Integer testCasesPassed;
    private Integer totalTestCases;
    private LocalDateTime submittedAt;
    private LocalDateTime completedAt;
    private List<TestCaseResult> testCaseResults;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestCaseResult {
        private Integer testCaseNumber;
        private Boolean passed;
        private Integer executionTimeMs;
        private String verdict;
    }
}

