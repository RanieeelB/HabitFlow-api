package com.habitflow.service;

import com.habitflow.dto.HabitRequestDTO;
import com.habitflow.dto.HabitResponseDTO;
import com.habitflow.entity.Habit;
import com.habitflow.entity.HabitHistory; // Import novo
import com.habitflow.entity.User;
import com.habitflow.repository.HabitHistoryRepository; // Import novo
import com.habitflow.repository.HabitRepository;
import com.habitflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;     // Import novo
import java.time.LocalDateTime; // Import novo
import java.util.List;
import java.util.Optional;      // Import novo

@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;
    private final HabitHistoryRepository habitHistoryRepository;

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
                habit.getFrequency(),
                false
        );
    }

    public List<HabitResponseDTO> getMyHabits() {
        User user = getCurrentUser();
        LocalDate today = LocalDate.now();


        List<Habit> habits = habitRepository.findAllByUserId(user.getId());


        List<HabitHistory> historyToday = habitHistoryRepository.findAllByHabit_User_IdAndDate(user.getId(), today);


        List<Long> completedHabitIds = historyToday.stream()
                .map(h -> h.getHabit().getId())
                .toList();


        return habits.stream()
                .map(habit -> new HabitResponseDTO(
                        habit.getId(),
                        habit.getName(),
                        habit.getDescription(),
                        habit.getFrequency(),
                        completedHabitIds.contains(habit.getId())
                ))
                .toList();
    }
    public void toggleCheckIn(Long habitId) {
        User user = getCurrentUser();

        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new RuntimeException("Hábito não encontrado"));

        if (!habit.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Você não tem permissão para alterar este hábito");
        }

        LocalDate today = LocalDate.now();
        Optional<HabitHistory> checkIn = habitHistoryRepository.findByHabitIdAndDate(habitId, today);

        if (checkIn.isPresent()) {
            habitHistoryRepository.delete(checkIn.get());
        } else {
            HabitHistory history = HabitHistory.builder()
                    .habit(habit)
                    .date(today)
                    .time(LocalDateTime.now())
                    .build();
            habitHistoryRepository.save(history);
        }


    }
}