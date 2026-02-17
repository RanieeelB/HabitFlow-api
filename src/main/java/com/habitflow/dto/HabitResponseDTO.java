package com.habitflow.dto;

import com.habitflow.entity.Frequency;

public record HabitResponseDTO(
        Long id,
        String name,
        String description,
        Frequency frequency,
        boolean completedToday,
        int streak
) {}