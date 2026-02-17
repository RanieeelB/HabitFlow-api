package com.habitflow.repository;

import com.habitflow.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HabitRepository extends JpaRepository<Habit, Long> {
    
    List<Habit> findAllByUserId(Long userId);
}