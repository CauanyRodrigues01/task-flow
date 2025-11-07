package com.taskflow.activityhistory;

<<<<<<< HEAD
=======
import com.taskflow.activityhistory.dto.ActivityHistoryResponseDto;
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
import com.taskflow.task.Task;
import com.taskflow.task.TaskRepository;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
<<<<<<< HEAD
=======
import com.taskflow.user.Role;
import org.junit.jupiter.api.BeforeEach;
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

<<<<<<< HEAD
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
=======
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)

@ExtendWith(MockitoExtension.class)
class ActivityHistoryServiceTest {

    @Mock
    private ActivityHistoryRepository activityHistoryRepository;
<<<<<<< HEAD

    @Mock
    private TaskRepository taskRepository;

=======
    @Mock
    private TaskRepository taskRepository;
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ActivityHistoryService activityHistoryService;

<<<<<<< HEAD
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
=======
    private User user;
    private Task task;
    private ActivityHistory activity1;
    private ActivityHistory activity2;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .role(Role.COLLABORATOR)
                .build();

        task = Task.builder()
                .id(100L)
                .title("Test Task")
                .build();

        activity1 = ActivityHistory.builder()
                .id(1L)
                .task(task)
                .user(user)
                .description("Task created")
                .createdAt(LocalDateTime.now().minusHours(1))
                .build();

        activity2 = ActivityHistory.builder()
                .id(2L)
                .task(task)
                .user(user)
                .description("Status changed")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void recordActivity_shouldSaveActivitySuccessfully() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(activityHistoryRepository.save(any(ActivityHistory.class))).thenReturn(activity1);

        activityHistoryService.recordActivity(task.getId(), user.getId(), "Test activity");

        verify(taskRepository, times(1)).findById(task.getId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(activityHistoryRepository, times(1)).save(any(ActivityHistory.class));
    }

    @Test
    void recordActivity_shouldSaveActivityWithNullUser_whenUserIdIsNull() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(activityHistoryRepository.save(any(ActivityHistory.class))).thenReturn(activity1);

        activityHistoryService.recordActivity(task.getId(), null, "System activity");

        verify(taskRepository, times(1)).findById(task.getId());
        verify(userRepository, never()).findById(anyLong());
        verify(activityHistoryRepository, times(1)).save(any(ActivityHistory.class));
    }

    @Test
    void recordActivity_shouldThrowException_whenTaskNotFound() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            activityHistoryService.recordActivity(task.getId(), user.getId(), "Test activity");
        });

        assertEquals("Task not found", exception.getMessage());
        verify(userRepository, never()).findById(anyLong());
        verify(activityHistoryRepository, never()).save(any(ActivityHistory.class));
    }

    @Test
    void recordActivity_shouldThrowException_whenUserNotFound() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            activityHistoryService.recordActivity(task.getId(), user.getId(), "Test activity");
        });

        assertEquals("User not found", exception.getMessage());
        verify(taskRepository, times(1)).findById(task.getId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(activityHistoryRepository, never()).save(any(ActivityHistory.class));
    }

    @Test
    void getActivitiesByTaskId_shouldReturnActivities() {
        when(activityHistoryRepository.findByTaskIdOrderByCreatedAtAsc(task.getId()))
                .thenReturn(Arrays.asList(activity1, activity2));

        List<ActivityHistoryResponseDto> results = activityHistoryService.getActivitiesByTaskId(task.getId());

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(activity1.getId(), results.get(0).getId());
        assertEquals(activity2.getId(), results.get(1).getId());
        verify(activityHistoryRepository, times(1)).findByTaskIdOrderByCreatedAtAsc(task.getId());
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
    }
}
