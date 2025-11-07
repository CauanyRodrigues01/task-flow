package com.taskflow.task;

import com.taskflow.activityhistory.ActivityHistoryService;
import com.taskflow.notification.NotificationService;
import com.taskflow.project.Project;
import com.taskflow.project.ProjectRepository;
import com.taskflow.task.dto.TaskRequestDto;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ActivityHistoryService activityHistoryService;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_shouldCreateTaskAndRecordActivity() {
        // Given
        Long projectId = 1L;
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTitle("Test Task");

        Project project = new Project();
        project.setId(projectId);

        Task taskToSave = Task.builder()
                .title(taskRequestDto.getTitle())
                .project(project)
                .build();
        
        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle(taskToSave.getTitle());
        savedTask.setProject(taskToSave.getProject());


        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // When
        taskService.createTask(projectId, taskRequestDto);

        // Then
        verify(taskRepository).save(any(Task.class));
        verify(activityHistoryService).recordActivity(anyLong(), any(), anyString());
    }
}
