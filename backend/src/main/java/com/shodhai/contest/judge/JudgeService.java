package com.shodhai.contest.judge;

import com.shodhai.contest.model.Problem;
import com.shodhai.contest.model.Submission;
import com.shodhai.contest.model.Submission.Status;
import com.shodhai.contest.model.TestCase;
import com.shodhai.contest.repository.ProblemRepository;
import com.shodhai.contest.repository.SubmissionRepository;
import com.shodhai.contest.repository.TestCaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class JudgeService {
    
    private final SubmissionRepository submissionRepository;
    private final ProblemRepository problemRepository;
    private final TestCaseRepository testCaseRepository;
    private final DockerExecutor dockerExecutor;
    private final LanguageStrategy languageStrategy;
    private final TestCaseValidator testCaseValidator;
    
    // Lazy to avoid circular dependency with SubmissionQueue
    @Lazy
    private SubmissionQueue submissionQueue;
    
    public JudgeService(
            SubmissionRepository submissionRepository,
            ProblemRepository problemRepository,
            TestCaseRepository testCaseRepository,
            DockerExecutor dockerExecutor,
            LanguageStrategy languageStrategy,
            TestCaseValidator testCaseValidator,
            @Lazy SubmissionQueue submissionQueue) {
        this.submissionRepository = submissionRepository;
        this.problemRepository = problemRepository;
        this.testCaseRepository = testCaseRepository;
        this.dockerExecutor = dockerExecutor;
        this.languageStrategy = languageStrategy;
        this.testCaseValidator = testCaseValidator;
        this.submissionQueue = submissionQueue;
    }
    
    public void queueSubmission(Long submissionId) {
        boolean added = submissionQueue.addSubmission(submissionId);
        if (!added) {
            updateSubmissionStatus(submissionId, Status.SYSTEM_ERROR, "Queue is full");
        }
    }
    
    @Transactional
    public void processSubmission(Long submissionId) {
        try {
            Submission submission = submissionRepository.findById(submissionId)
                    .orElseThrow(() -> new RuntimeException("Submission not found: " + submissionId));
            
            // Update status to RUNNING
            submission.setStatus(Status.RUNNING);
            submissionRepository.save(submission);
            
            // Get problem and test cases
            Problem problem = submission.getProblem();
            List<TestCase> testCases = testCaseRepository.findByProblemIdOrderByDisplayOrderAsc(problem.getId());
            
            if (testCases.isEmpty()) {
                updateSubmissionStatus(submissionId, Status.SYSTEM_ERROR, "No test cases found");
                return;
            }
            
            // Get language configuration
            LanguageStrategy.LanguageConfig langConfig = languageStrategy.getConfig(submission.getLanguage());
            
            // Run all test cases
            int totalScore = 0;
            int passedCount = 0;
            int maxExecutionTime = 0;
            String finalVerdict = "ACCEPTED";
            
            for (int i = 0; i < testCases.size(); i++) {
                TestCase testCase = testCases.get(i);
                
                log.info("Submission {}: Running test case {}/{}", submissionId, i + 1, testCases.size());
                
                // Execute code with test case input
                DockerExecutor.ExecutionRequest request = DockerExecutor.ExecutionRequest.builder()
                        .submissionId(submissionId)
                        .code(submission.getCode())
                        .fileName(langConfig.getFileName())
                        .input(testCase.getInput())
                        .needsCompilation(langConfig.isNeedsCompilation())
                        .compileCommand(langConfig.getCompileCommand())
                        .runCommand(langConfig.getRunCommand())
                        .timeLimitMs(problem.getTimeLimitMs())
                        .build();
                
                DockerExecutor.ExecutionResult execResult = dockerExecutor.execute(request);
                
                // Check execution result
                if (!execResult.isSuccess()) {
                    // Execution failed (TLE, MLE, RE, CE)
                    finalVerdict = execResult.getVerdict();
                    submission.setErrorMessage(execResult.getErrorMessage());
                    break;
                }
                
                // Validate output
                TestCaseValidator.ValidationResult validationResult = 
                        testCaseValidator.validate(execResult.getOutput(), testCase.getExpectedOutput());
                
                if (validationResult.isPassed()) {
                    totalScore += testCase.getPoints();
                    passedCount++;
                    maxExecutionTime = Math.max(maxExecutionTime, execResult.getExecutionTimeMs());
                } else {
                    finalVerdict = "WRONG_ANSWER";
                    break;
                }
            }
            
            // Update submission with results
            submission.setScore(totalScore);
            submission.setTestCasesPassed(passedCount);
            submission.setTotalTestCases(testCases.size());
            submission.setExecutionTimeMs(maxExecutionTime);
            submission.setCompletedAt(LocalDateTime.now());
            
            // Set final status
            if ("ACCEPTED".equals(finalVerdict) && passedCount == testCases.size()) {
                submission.setStatus(Status.ACCEPTED);
                submission.setResult("AC");
            } else {
                switch (finalVerdict) {
                    case "WRONG_ANSWER":
                        submission.setStatus(Status.WRONG_ANSWER);
                        submission.setResult("WA");
                        break;
                    case "TIME_LIMIT_EXCEEDED":
                        submission.setStatus(Status.TIME_LIMIT_EXCEEDED);
                        submission.setResult("TLE");
                        break;
                    case "MEMORY_LIMIT_EXCEEDED":
                        submission.setStatus(Status.MEMORY_LIMIT_EXCEEDED);
                        submission.setResult("MLE");
                        break;
                    case "RUNTIME_ERROR":
                        submission.setStatus(Status.RUNTIME_ERROR);
                        submission.setResult("RE");
                        break;
                    case "COMPILATION_ERROR":
                        submission.setStatus(Status.COMPILATION_ERROR);
                        submission.setResult("CE");
                        break;
                    default:
                        submission.setStatus(Status.SYSTEM_ERROR);
                        submission.setResult("SE");
                }
            }
            
            submissionRepository.save(submission);
            log.info("Submission {} completed with verdict: {}, score: {}/{}", 
                    submissionId, submission.getStatus(), passedCount, testCases.size());
            
        } catch (Exception e) {
            log.error("Error processing submission {}", submissionId, e);
            updateSubmissionStatus(submissionId, Status.SYSTEM_ERROR, e.getMessage());
        }
    }
    
    private void updateSubmissionStatus(Long submissionId, Status status, String errorMessage) {
        try {
            Submission submission = submissionRepository.findById(submissionId)
                    .orElseThrow(() -> new RuntimeException("Submission not found"));
            submission.setStatus(status);
            submission.setErrorMessage(errorMessage);
            submission.setCompletedAt(LocalDateTime.now());
            submissionRepository.save(submission);
        } catch (Exception e) {
            log.error("Failed to update submission status", e);
        }
    }
}

