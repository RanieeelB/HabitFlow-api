package com.habitflow.service;

import com.habitflow.dto.HabitRequestDTO;
import com.habitflow.dto.HabitResponseDTO;
import com.habitflow.entity.Frequency;
import com.habitflow.entity.Habit;
import com.habitflow.entity.HabitHistory;
import com.habitflow.entity.User;
import com.habitflow.exception.NotFoundException;
import com.habitflow.repository.HabitHistoryRepository;
import com.habitflow.repository.HabitRepository;
import com.habitflow.repository.UserRepository;
import com.habitflow.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;
    private final HabitHistoryRepository habitHistoryRepository;


    private final AuthenticatedUserService authUserService;

    private User getCurrentUser() {
        String email = authUserService.getEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
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
                false,
                0
        );
    }

    public Page<HabitResponseDTO> getMyHabits(
            String search,
            Frequency frequency,
            Boolean completedToday,
            Pageable pageable
    ) {
        User user = getCurrentUser();
        LocalDate today = LocalDate.now();

        Page<Habit> habitsPage = habitRepository.findWithFilters(
                user.getId(),
                search,
                frequency,
                pageable
        );

        List<HabitHistory> historyToday =
                habitHistoryRepository.findAllByHabit_User_IdAndDate(user.getId(), today);

        List<Long> completedHabitIds = historyToday.stream()
                .map(h -> h.getHabit().getId())
                .toList();

        List<HabitResponseDTO> dtoList = habitsPage.getContent().stream()
                .filter(habit -> {
                    if (completedToday == null) return true;

                    boolean isCompleted = completedHabitIds.contains(habit.getId());
                    return completedToday ? isCompleted : !isCompleted;
                })
                .map(habit -> new HabitResponseDTO(
                        habit.getId(),
                        habit.getName(),
                        habit.getDescription(),
                        habit.getFrequency(),
                        completedHabitIds.contains(habit.getId()),
                        calculateStreak(habit.getId())
                ))
                .toList();

        return new org.springframework.data.domain.PageImpl<>(
                dtoList,
                pageable,
                habitsPage.getTotalElements()
        );
    }


    public void toggleCheckIn(Long habitId) throws NotFoundException {
        User user = getCurrentUser();

        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new NotFoundException("Hábito não encontrado"));

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

    private int calculateStreak(Long habitId) {
        List<HabitHistory> history = habitHistoryRepository.findAllByHabitIdOrderByDateDesc(habitId);

        if (history.isEmpty()) return 0;

        int streak = 0;
        LocalDate expectedDate = LocalDate.now();

        // Se não fez hoje, começamos a verificar a partir de ontem
        if (!history.get(0).getDate().equals(expectedDate)) {
            expectedDate = expectedDate.minusDays(1);
        }

        for (HabitHistory log : history) {
            if (log.getDate().equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else if (log.getDate().isBefore(expectedDate)) {
                break;
            }
        }

        return streak;
    }

}