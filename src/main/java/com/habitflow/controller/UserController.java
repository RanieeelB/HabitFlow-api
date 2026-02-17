package com.habitflow.controller;

import com.habitflow.dto.UserResponseDTO;
import com.habitflow.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Endpoint profissional: retorna o usuário autenticado
    @GetMapping("/me")
    public UserResponseDTO getMe() {
        return userService.getMe();
    }
}
