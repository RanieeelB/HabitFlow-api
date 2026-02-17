package com.habitflow.controller;

import com.habitflow.dto.HabitRequestDTO;
import com.habitflow.dto.HabitResponseDTO;
import com.habitflow.entity.Frequency;
import com.habitflow.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/api/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;

    @PostMapping
    public ResponseEntity<HabitResponseDTO> createHabit(@RequestBody HabitRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(habitService.createHabit(request));
    }

    @GetMapping("/my")
    public Page<HabitResponseDTO> getMyHabits(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Frequency frequency,
            @RequestParam(required = false) Boolean completedToday,
            Pageable pageable
    ) {
        return habitService.getMyHabits(search, frequency, completedToday, pageable);
    }

    @PostMapping("/{id}/check")
    public ResponseEntity<Void> toggleCheckIn(@PathVariable Long id) {
        habitService.toggleCheckIn(id);
        return ResponseEntity.ok().build();
    }
}