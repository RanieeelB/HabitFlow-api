package com.habitflow.controller;

import com.habitflow.dto.HabitRequestDTO;
import com.habitflow.dto.HabitResponseDTO;
import com.habitflow.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;

    @PostMapping
    public ResponseEntity<HabitResponseDTO> createHabit(@RequestBody HabitRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(habitService.createHabit(request));
    }

    @GetMapping
    public ResponseEntity<List<HabitResponseDTO>> getMyHabits() {
        return ResponseEntity.ok(habitService.getMyHabits());
    }

    @PostMapping("/{id}/check")
    public ResponseEntity<Void> toggleCheckIn(@PathVariable Long id) {
        habitService.toggleCheckIn(id);
        return ResponseEntity.ok().build();
    }
}