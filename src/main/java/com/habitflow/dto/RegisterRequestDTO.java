package com.habitflow.dto;

import jakarta.validation.constraints.*;

public record RegisterRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100)
        String name,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String password
) {}
