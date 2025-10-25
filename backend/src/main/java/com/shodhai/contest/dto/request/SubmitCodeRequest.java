package com.shodhai.contest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitCodeRequest {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Problem ID is required")
    private Long problemId;
    
    @NotNull(message = "Contest ID is required")
    private Long contestId;
    
    @NotBlank(message = "Code is required")
    @Size(max = 10000, message = "Code must not exceed 10000 characters")
    private String code;
    
    @NotBlank(message = "Language is required")
    private String language;
}

