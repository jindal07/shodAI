package com.shodhai.contest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDetailResponse {
    private Long id;
    private String title;
    private String description;
    private String difficulty;
    private Integer timeLimitMs;
    private Integer memoryLimitMb;
    private List<TestCaseSummary> sampleTestCases;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestCaseSummary {
        private Long id;
        private String input;
        private String expectedOutput;
    }
}

