package com.shodhai.contest.repository;

import com.shodhai.contest.model.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
    Optional<Contest> findByIdAndIsActiveTrue(Long id);
    List<Contest> findByIsActiveTrueOrderByStartTimeDesc();
    
    @Query("SELECT c FROM Contest c LEFT JOIN FETCH c.problems WHERE c.id = :id AND c.isActive = true")
    Optional<Contest> findByIdWithProblems(Long id);
}

