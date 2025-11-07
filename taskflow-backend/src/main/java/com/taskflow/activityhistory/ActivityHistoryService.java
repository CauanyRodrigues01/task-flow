package com.taskflow.activityhistory;

<<<<<<< HEAD
import com.taskflow.activityhistory.dto.ActivityHistoryResponse;
=======
import com.taskflow.activityhistory.dto.ActivityHistoryResponseDto;
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
import com.taskflow.task.Task;
import com.taskflow.task.TaskRepository;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
<<<<<<< HEAD

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
=======
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityHistoryService {

    private final ActivityHistoryRepository activityHistoryRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

<<<<<<< HEAD
    public void recordActivity(Long taskId, Long userId, String description) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
=======
    @Transactional
    public void recordActivity(Long taskId, Long userId, String description) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)

        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId)
<<<<<<< HEAD
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        }

        ActivityHistory history = ActivityHistory.builder()
                .task(task)
                .user(user)
                .description(description)
                .build();

        activityHistoryRepository.save(history);
    }

    public List<ActivityHistoryResponse> getTaskHistory(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));

        return activityHistoryRepository.findByTaskIdOrderByCreatedAtDesc(taskId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ActivityHistoryResponse convertToDto(ActivityHistory history) {
        Long userId = (history.getUser() != null) ? history.getUser().getId() : null;
        return new ActivityHistoryResponse(history.getId(), history.getTask().getId(), userId, history.getDescription(), history.getCreatedAt());
=======
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        ActivityHistory activity = ActivityHistory.builder()
                .task(task)
                .user(user)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();

        activityHistoryRepository.save(activity);
    }

    @Transactional(readOnly = true)
    public List<ActivityHistoryResponseDto> getActivitiesByTaskId(Long taskId) {
        List<ActivityHistory> activities = activityHistoryRepository.findByTaskIdOrderByCreatedAtAsc(taskId);
        return activities.stream()
                .map(this::mapToActivityHistoryResponseDto)
                .collect(Collectors.toList());
    }

    private ActivityHistoryResponseDto mapToActivityHistoryResponseDto(ActivityHistory activity) {
        ActivityHistoryResponseDto dto = new ActivityHistoryResponseDto();
        dto.setId(activity.getId());
        dto.setTaskId(activity.getTask().getId());
        dto.setUserId(activity.getUser() != null ? activity.getUser().getId() : null);
        dto.setUserName(activity.getUser() != null ? activity.getUser().getName() : "Sistema");
        dto.setDescription(activity.getDescription());
        dto.setCreatedAt(activity.getCreatedAt());
        return dto;
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
    }
}
