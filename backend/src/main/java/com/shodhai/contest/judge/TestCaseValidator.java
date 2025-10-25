package com.shodhai.contest.judge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
public class TestCaseValidator {
    
    public ValidationResult validate(String actualOutput, String expectedOutput) {
        if (actualOutput == null || expectedOutput == null) {
            return ValidationResult.builder()
                    .passed(false)
                    .verdict("WRONG_ANSWER")
                    .build();
        }
        
        // Normalize outputs (trim trailing whitespace, normalize line endings)
        String normalizedActual = normalizeOutput(actualOutput);
        String normalizedExpected = normalizeOutput(expectedOutput);
        
        boolean passed = normalizedActual.equals(normalizedExpected);
        
        return ValidationResult.builder()
                .passed(passed)
                .verdict(passed ? "ACCEPTED" : "WRONG_ANSWER")
                .build();
    }
    
    private String normalizeOutput(String output) {
        if (output == null) {
            return "";
        }
        
        // Split into lines, trim each line, remove empty lines at end
        String[] lines = output.split("\\r?\\n");
        StringBuilder normalized = new StringBuilder();
        
        for (int i = 0; i < lines.length; i++) {
            String trimmed = lines[i].trim();
            normalized.append(trimmed);
            if (i < lines.length - 1) {
                normalized.append("\n");
            }
        }
        
        // Remove trailing newlines
        String result = normalized.toString();
        while (result.endsWith("\n")) {
            result = result.substring(0, result.length() - 1);
        }
        
        return result;
    }
    
    @Data
    @Builder
    @AllArgsConstructor
    public static class ValidationResult {
        private boolean passed;
        private String verdict;
    }
}

