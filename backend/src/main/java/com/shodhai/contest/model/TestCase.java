package com.shodhai.contest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "test_cases", indexes = {
    @Index(name = "idx_problem_id", columnList = "problem_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;
    
    @Column(columnDefinition = "TEXT")
    private String input;
    
    @Column(name = "expected_output", columnDefinition = "TEXT")
    private String expectedOutput;
    
    @Column(name = "is_sample", nullable = false)
    private Boolean isSample = false;
    
    @Column(nullable = false)
    private Integer points = 10;
    
    @Column(name = "display_order")
    private Integer displayOrder = 0;
}

