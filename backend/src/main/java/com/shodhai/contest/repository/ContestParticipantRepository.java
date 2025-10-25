package com.shodhai.contest.repository;

import com.shodhai.contest.model.ContestParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContestParticipantRepository extends JpaRepository<ContestParticipant, Long> {
    Optional<ContestParticipant> findByContestIdAndUserId(Long contestId, Long userId);
    boolean existsByContestIdAndUserId(Long contestId, Long userId);
    List<ContestParticipant> findByContestIdOrderByTotalScoreDescJoinedAtAsc(Long contestId);
    
    @Query("SELECT COUNT(p) FROM ContestParticipant p WHERE p.contest.id = :contestId")
    Long countByContestId(Long contestId);
}

