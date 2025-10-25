package com.shodhai.contest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "contest_participants", 
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_contest_user", columnNames = {"contest_id", "user_id"})
    },
    indexes = {
        @Index(name = "idx_contest_id", columnList = "contest_id"),
        @Index(name = "idx_user_id", columnList = "user_id")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestParticipant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;
    
    @Column(name = "total_score")
    private Integer totalScore = 0;
    
    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
        if (totalScore == null) {
            totalScore = 0;
        }
    }
}

