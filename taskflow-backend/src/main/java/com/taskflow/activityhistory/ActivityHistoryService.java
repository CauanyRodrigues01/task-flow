package com.taskflow.activityhistory;

import com.taskflow.task.Task;
import com.taskflow.task.TaskRepository;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

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
}
