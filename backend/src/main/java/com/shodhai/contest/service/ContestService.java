package com.shodhai.contest.service;

import com.shodhai.contest.dto.request.JoinContestRequest;
import com.shodhai.contest.dto.response.ContestResponse;
import com.shodhai.contest.dto.response.ProblemDetailResponse;
import com.shodhai.contest.exception.ResourceNotFoundException;
import com.shodhai.contest.model.Contest;
import com.shodhai.contest.model.ContestParticipant;
import com.shodhai.contest.model.Problem;
import com.shodhai.contest.model.TestCase;
import com.shodhai.contest.model.User;
import com.shodhai.contest.repository.ContestParticipantRepository;
import com.shodhai.contest.repository.ContestRepository;
import com.shodhai.contest.repository.ProblemRepository;
import com.shodhai.contest.repository.TestCaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContestService {
    
    private final ContestRepository contestRepository;
    private final ProblemRepository problemRepository;
    private final TestCaseRepository testCaseRepository;
    private final ContestParticipantRepository participantRepository;
    private final UserService userService;
    
    public ContestResponse getContestById(Long contestId) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new ResourceNotFoundException("Contest", "id", contestId));
        
        List<Problem> problems = problemRepository.findByContestIdOrderByDisplayOrderAsc(contestId);
        
        return ContestResponse.builder()
                .id(contest.getId())
                .title(contest.getTitle())
                .description(contest.getDescription())
                .startTime(contest.getStartTime())
                .endTime(contest.getEndTime())
                .isActive(contest.getIsActive())
                .problems(problems.stream()
                        .map(p -> ContestResponse.ProblemSummary.builder()
                                .id(p.getId())
                                .title(p.getTitle())
                                .difficulty(p.getDifficulty().name())
                                .timeLimitMs(p.getTimeLimitMs())
                                .memoryLimitMb(p.getMemoryLimitMb())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
    
    public ProblemDetailResponse getProblemById(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResourceNotFoundException("Problem", "id", problemId));
        
        List<TestCase> sampleTestCases = testCaseRepository
                .findByProblemIdAndIsSampleTrueOrderByDisplayOrderAsc(problemId);
        
        return ProblemDetailResponse.builder()
                .id(problem.getId())
                .title(problem.getTitle())
                .description(problem.getDescription())
                .difficulty(problem.getDifficulty().name())
                .timeLimitMs(problem.getTimeLimitMs())
                .memoryLimitMb(problem.getMemoryLimitMb())
                .sampleTestCases(sampleTestCases.stream()
                        .map(tc -> ProblemDetailResponse.TestCaseSummary.builder()
                                .id(tc.getId())
                                .input(tc.getInput())
                                .expectedOutput(tc.getExpectedOutput())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
    
    @Transactional
    public User joinContest(Long contestId, JoinContestRequest request) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new ResourceNotFoundException("Contest", "id", contestId));
        
        if (!contest.getIsActive()) {
            throw new IllegalArgumentException("Contest is not active");
        }
        
        User user = userService.findOrCreateUser(request.getUsername(), request.getEmail());
        
        // Check if already joined
        if (!participantRepository.existsByContestIdAndUserId(contestId, user.getId())) {
            ContestParticipant participant = new ContestParticipant();
            participant.setContest(contest);
            participant.setUser(user);
            participant.setJoinedAt(LocalDateTime.now());
            participant.setTotalScore(0);
            participantRepository.save(participant);
            log.info("User {} joined contest {}", user.getUsername(), contestId);
        }
        
        return user;
    }
    
    public List<Contest> getAllActiveContests() {
        return contestRepository.findByIsActiveTrueOrderByStartTimeDesc();
    }
}

