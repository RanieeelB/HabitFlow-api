package com.habitflow.repository;

import com.habitflow.entity.HabitHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HabitHistoryRepository extends JpaRepository<HabitHistory, Long> {

    List<HabitHistory> findAllByHabitId(Long habitId);

    Optional<HabitHistory> findByHabitIdAndDate(Long habitId, LocalDate date);

    List<HabitHistory> findAllByHabit_User_IdAndDate(Long userId, LocalDate date);

    List<HabitHistory> findAllByHabitIdOrderByDateDesc(Long habitId);
}