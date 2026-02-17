package com.habitflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "habit_history")
public class HabitHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Habit habit;
}