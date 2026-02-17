package com.habitflow.dto;

import com.habitflow.entity.Frequency;

public record HabitRequestDTO(
        String name,
        String description,
        Frequency frequency
) {}