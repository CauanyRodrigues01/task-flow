package com.taskflow.activityhistory;

import com.taskflow.activityhistory.dto.ActivityHistoryResponse;
import com.taskflow.task.Task;
import com.taskflow.task.TaskRepository;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityHistoryService {

    private final ActivityHistoryRepository activityHistoryRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public void recordActivity(Long taskId, Long userId, String description) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));

        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId)
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
    }
}
