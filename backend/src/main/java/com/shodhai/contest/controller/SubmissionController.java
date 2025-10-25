package com.shodhai.contest.controller;

import com.shodhai.contest.dto.request.SubmitCodeRequest;
import com.shodhai.contest.dto.response.ApiResponse;
import com.shodhai.contest.dto.response.SubmissionResponse;
import com.shodhai.contest.service.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
@Slf4j
public class SubmissionController {
    
    private final SubmissionService submissionService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<SubmissionResponse>> submitCode(
            @Valid @RequestBody SubmitCodeRequest request) {
        log.info("POST /api/submissions - User: {}, Problem: {}, Language: {}", 
                request.getUserId(), request.getProblemId(), request.getLanguage());
        
        SubmissionResponse submission = submissionService.submitCode(request);
        
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(ApiResponse.success("Submission received and queued for judging", submission));
    }
    
    @GetMapping("/{submissionId}")
    public ResponseEntity<ApiResponse<SubmissionResponse>> getSubmissionStatus(
            @PathVariable Long submissionId) {
        log.debug("GET /api/submissions/{}", submissionId);
        
        SubmissionResponse submission = submissionService.getSubmissionStatus(submissionId);
        
        return ResponseEntity.ok(ApiResponse.success(submission));
    }
}

