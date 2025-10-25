package com.shodhai.contest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_problem_id", columnList = "problem_id"),
    @Index(name = "idx_contest_user", columnList = "contest_id, user_id"),
    @Index(name = "idx_submitted_at", columnList = "submitted_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Submission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String code;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Language language;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.PENDING;
    
    @Column(length = 50)
    private String result;
    
    @Column
    private Integer score = 0;
    
    @Column(name = "execution_time_ms")
    private Integer executionTimeMs;
    
    @Column(name = "memory_used_mb")
    private Integer memoryUsedMb;
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(name = "test_cases_passed")
    private Integer testCasesPassed = 0;
    
    @Column(name = "total_test_cases")
    private Integer totalTestCases = 0;
    
    @Column(name = "submitted_at", nullable = false, updatable = false)
    private LocalDateTime submittedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @PrePersist
    protected void onCreate() {
        submittedAt = LocalDateTime.now();
        if (status == null) {
            status = Status.PENDING;
        }
        if (score == null) {
            score = 0;
        }
    }
    
    public enum Language {
        JAVA, PYTHON, CPP, JAVASCRIPT
    }
    
    public enum Status {
        PENDING, RUNNING, ACCEPTED, WRONG_ANSWER, TIME_LIMIT_EXCEEDED, 
        MEMORY_LIMIT_EXCEEDED, RUNTIME_ERROR, COMPILATION_ERROR, SYSTEM_ERROR
    }
}

