package com.habitflow.dto;

import com.habitflow.entity.Role;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        Role role,
        LocalDateTime createdAt
) {}
