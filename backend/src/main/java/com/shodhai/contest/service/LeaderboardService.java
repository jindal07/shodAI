package com.shodhai.contest.service;

import com.shodhai.contest.dto.response.LeaderboardResponse;
import com.shodhai.contest.dto.response.LeaderboardResponse.LeaderboardEntry;
import com.shodhai.contest.exception.ResourceNotFoundException;
import com.shodhai.contest.model.Contest;
import com.shodhai.contest.repository.ContestRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaderboardService {
    
    private final ContestRepository contestRepository;
    private final EntityManager entityManager;
    
    public LeaderboardResponse getLeaderboard(Long contestId) {
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new ResourceNotFoundException("Contest", "id", contestId));
        
        // Calculate leaderboard using native query for better performance
        String sql = """
            WITH user_scores AS (
                SELECT 
                    s.user_id,
                    u.username,
                    s.problem_id,
                    MAX(s.score) as best_score,
                    MIN(s.submitted_at) as first_accepted_time
                FROM submissions s
                JOIN users u ON s.user_id = u.id
                WHERE s.contest_id = :contestId
                  AND s.status = 'ACCEPTED'
                GROUP BY s.user_id, u.username, s.problem_id
            ),
            total_scores AS (
                SELECT 
                    user_id,
                    username,
                    SUM(best_score) as total_score,
                    COUNT(DISTINCT problem_id) as problems_solved,
                    MAX(first_accepted_time) as last_submission_time
                FROM user_scores
                GROUP BY user_id, username
            )
            SELECT 
                user_id,
                username,
                total_score,
                problems_solved,
                last_submission_time
            FROM total_scores
            ORDER BY total_score DESC, last_submission_time ASC
            LIMIT 100
            """;
        
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("contestId", contestId);
        
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        
        List<LeaderboardEntry> entries = new ArrayList<>();
        int rank = 1;
        
        for (Object[] row : results) {
            LeaderboardEntry entry = LeaderboardEntry.builder()
                    .rank(rank++)
                    .userId(((BigInteger) row[0]).longValue())
                    .username((String) row[1])
                    .totalScore(((Number) row[2]).intValue())
                    .problemsSolved(((BigInteger) row[3]).intValue())
                    .lastSubmissionTime(((Timestamp) row[4]).toLocalDateTime())
                    .build();
            entries.add(entry);
        }
        
        return LeaderboardResponse.builder()
                .contestId(contestId)
                .lastUpdated(LocalDateTime.now())
                .entries(entries)
                .build();
    }
}

