package com.shodhai.contest.repository;

import com.shodhai.contest.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    List<TestCase> findByProblemIdOrderByDisplayOrderAsc(Long problemId);
    List<TestCase> findByProblemIdAndIsSampleTrueOrderByDisplayOrderAsc(Long problemId);
}

