package com.taskflow.activityhistory;

import com.taskflow.task.Task;
import com.taskflow.task.TaskRepository;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivityHistoryServiceTest {

    @Mock
    private ActivityHistoryRepository activityHistoryRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ActivityHistoryService activityHistoryService;

    @Test
    void recordActivity_shouldSaveActivityHistory() {
        // Given
        Long taskId = 1L;
        Long userId = 1L;
        String description = "Test activity";

        Task task = new Task();
        task.setId(taskId);

        User user = new User();
        user.setId(userId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        activityHistoryService.recordActivity(taskId, userId, description);

        // Then
        verify(activityHistoryRepository).save(any(ActivityHistory.class));
    }
}
