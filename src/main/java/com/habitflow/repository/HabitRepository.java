package com.habitflow.repository;

import com.habitflow.entity.Frequency;
import com.habitflow.entity.Habit;
import com.habitflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface HabitRepository extends JpaRepository<Habit, Long> {

    Page<Habit> findAllByUserId(Long userId, Pageable pageable);

    @Query("""
    SELECT h FROM Habit h
    WHERE h.user.id = :userId
    AND (:search IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%', :search, '%')))
    AND (:frequency IS NULL OR h.frequency = :frequency)
""")
    Page<Habit> findWithFilters(
            Long userId,
            String search,
            Frequency frequency,
            Pageable pageable
    );
}
