package com.shodhai.contest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "problems", indexes = {
    @Index(name = "idx_contest_id", columnList = "contest_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Problem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(length = 5000)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Difficulty difficulty;
    
    @Column(name = "time_limit_ms", nullable = false)
    private Integer timeLimitMs = 1000;
    
    @Column(name = "memory_limit_mb", nullable = false)
    private Integer memoryLimitMb = 256;
    
    @Column(name = "display_order")
    private Integer displayOrder = 0;
    
    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TestCase> testCases = new ArrayList<>();
    
    public enum Difficulty {
        EASY, MEDIUM, HARD
    }
}

