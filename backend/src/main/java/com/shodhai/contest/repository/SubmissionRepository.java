package com.shodhai.contest.repository;

import com.shodhai.contest.model.Submission;
import com.shodhai.contest.model.Submission.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByUserIdOrderBySubmittedAtDesc(Long userId);
    List<Submission> findByContestIdAndUserIdOrderBySubmittedAtDesc(Long contestId, Long userId);
    
    @Query("SELECT s FROM Submission s WHERE s.problem.id = :problemId AND s.user.id = :userId AND s.status = :status")
    List<Submission> findByProblemIdAndUserIdAndStatus(Long problemId, Long userId, Status status);
    
    @Query("SELECT COALESCE(MAX(s.score), 0) FROM Submission s WHERE s.problem.id = :problemId AND s.user.id = :userId")
    Integer findMaxScoreByProblemIdAndUserId(Long problemId, Long userId);
}

