package com.habitflow.service;

import com.habitflow.dto.HabitRequestDTO;
import com.habitflow.dto.HabitResponseDTO;
import com.habitflow.entity.Habit;
import com.habitflow.entity.User;
import com.habitflow.repository.HabitRepository;
import com.habitflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    public HabitResponseDTO createHabit(HabitRequestDTO request) {
        User user = getCurrentUser();


        Habit habit = Habit.builder()
                .name(request.name())
                .description(request.description())
                .frequency(request.frequency())
                .user(user)
                .build();

        habitRepository.save(habit);


        return new HabitResponseDTO(
                habit.getId(),
                habit.getName(),
                habit.getDescription(),
                habit.getFrequency()
        );
    }

    public List<HabitResponseDTO> getMyHabits() {
        User user = getCurrentUser();


        List<Habit> habits = habitRepository.findAllByUserId(user.getId());


        return habits.stream()
                .map(habit -> new HabitResponseDTO(
                        habit.getId(),
                        habit.getName(),
                        habit.getDescription(),
                        habit.getFrequency()
                ))
                .toList();
    }
}