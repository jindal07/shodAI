package com.shodhai.contest.service;

import com.shodhai.contest.dto.request.SubmitCodeRequest;
import com.shodhai.contest.dto.response.SubmissionResponse;
import com.shodhai.contest.exception.ResourceNotFoundException;
import com.shodhai.contest.judge.JudgeService;
import com.shodhai.contest.model.Contest;
import com.shodhai.contest.model.Problem;
import com.shodhai.contest.model.Submission;
import com.shodhai.contest.model.Submission.Language;
import com.shodhai.contest.model.Submission.Status;
import com.shodhai.contest.model.User;
import com.shodhai.contest.repository.ContestRepository;
import com.shodhai.contest.repository.ProblemRepository;
import com.shodhai.contest.repository.SubmissionRepository;
import com.shodhai.contest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmissionService {
    
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final ContestRepository contestRepository;
    private final JudgeService judgeService;
    
    @Transactional
    public SubmissionResponse submitCode(SubmitCodeRequest request) {
        // Validate user, problem, and contest
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));
        
        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new ResourceNotFoundException("Problem", "id", request.getProblemId()));
        
        Contest contest = contestRepository.findById(request.getContestId())
                .orElseThrow(() -> new ResourceNotFoundException("Contest", "id", request.getContestId()));
        
        // Parse language
        Language language;
        try {
            language = Language.valueOf(request.getLanguage().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported language: " + request.getLanguage());
        }
        
        // Create submission
        Submission submission = new Submission();
        submission.setUser(user);
        submission.setProblem(problem);
        submission.setContest(contest);
        submission.setCode(request.getCode());
        submission.setLanguage(language);
        submission.setStatus(Status.PENDING);
        submission.setScore(0);
        submission.setTestCasesPassed(0);
        submission.setTotalTestCases(0);
        submission.setSubmittedAt(LocalDateTime.now());
        
        Submission savedSubmission = submissionRepository.save(submission);
        log.info("Created submission {} for user {} on problem {}", 
                savedSubmission.getId(), user.getUsername(), problem.getTitle());
        
        // Add to judge queue
        judgeService.queueSubmission(savedSubmission.getId());
        
        return buildSubmissionResponse(savedSubmission);
    }
    
    public SubmissionResponse getSubmissionStatus(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission", "id", submissionId));
        
        return buildSubmissionResponse(submission);
    }
    
    private SubmissionResponse buildSubmissionResponse(Submission submission) {
        return SubmissionResponse.builder()
                .id(submission.getId())
                .userId(submission.getUser().getId())
                .problemId(submission.getProblem().getId())
                .contestId(submission.getContest().getId())
                .language(submission.getLanguage().name())
                .status(submission.getStatus().name())
                .result(submission.getResult())
                .score(submission.getScore())
                .executionTimeMs(submission.getExecutionTimeMs())
                .memoryUsedMb(submission.getMemoryUsedMb())
                .errorMessage(submission.getErrorMessage())
                .testCasesPassed(submission.getTestCasesPassed())
                .totalTestCases(submission.getTotalTestCases())
                .submittedAt(submission.getSubmittedAt())
                .completedAt(submission.getCompletedAt())
                .testCaseResults(new ArrayList<>()) // Could be expanded with detailed results
                .build();
    }
}

