package com.taskflow.task;

import com.taskflow.activityhistory.ActivityHistoryService;
import com.taskflow.notification.NotificationService;
import com.taskflow.project.Project;
import com.taskflow.project.ProjectRepository;
import com.taskflow.task.dto.TaskRequestDto;
<<<<<<< HEAD
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
=======
import com.taskflow.task.dto.TaskStatusUpdateRequestDto;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
=======
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
<<<<<<< HEAD

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

=======
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private NotificationService notificationService;
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
    @Mock
    private ActivityHistoryService activityHistoryService;

    @InjectMocks
    private TaskService taskService;

<<<<<<< HEAD
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
=======
    private User currentUser;
    private User assigneeUser;
    private Project project;
    private Task task;
    private TaskRequestDto taskRequestDto;

    @BeforeEach
    void setUp() {
        currentUser = User.builder()
                .id(1L)
                .name("Current User")
                .email("current@example.com")
                .role(Role.PROJECT_MANAGER)
                .build();

        assigneeUser = User.builder()
                .id(2L)
                .name("Assignee User")
                .email("assignee@example.com")
                .role(Role.COLLABORATOR)
                .build();

        project = Project.builder()
                .id(10L)
                .name("Test Project")
                .owner(currentUser)
                .build();

        task = Task.builder()
                .id(100L)
                .title("Original Title")
                .description("Original Description")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.MEDIUM)
                .dueDate(LocalDateTime.now().plusDays(5))
                .project(project)
                .assignee(assigneeUser)
                .build();

        taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTitle("New Title");
        taskRequestDto.setDescription("New Description");
        taskRequestDto.setStatus(TaskStatus.IN_PROGRESS);
        taskRequestDto.setPriority(TaskPriority.HIGH);
        taskRequestDto.setDueDate(LocalDateTime.now().plusDays(10));
        taskRequestDto.setAssigneeId(assigneeUser.getId());
    }

    @Test
    void createTask_shouldCreateTaskAndRecordActivityAndNotifyAssignee() {
        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(userRepository.findById(assigneeUser.getId())).thenReturn(Optional.of(assigneeUser));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        taskService.createTask(project.getId(), taskRequestDto, currentUser);

        verify(taskRepository, times(1)).save(any(Task.class));
        verify(notificationService, times(1)).createNotification(eq(assigneeUser.getId()), anyString());
        verify(activityHistoryService, times(1)).recordActivity(eq(task.getId()), eq(currentUser.getId()), anyString());
    }

    @Test
    void updateTask_shouldUpdateTaskAndRecordActivitiesForChanges() {
        Task updatedTask = Task.builder()
                .id(task.getId())
                .title("Updated Title")
                .description("Updated Description")
                .status(TaskStatus.DONE)
                .priority(TaskPriority.LOW)
                .dueDate(LocalDateTime.now().plusDays(15))
                .project(project)
                .assignee(currentUser)
                .build();

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(userRepository.findById(assigneeUser.getId())).thenReturn(Optional.of(assigneeUser));
        when(userRepository.findById(currentUser.getId())).thenReturn(Optional.of(currentUser));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        taskService.updateTask(task.getId(), taskRequestDto, currentUser);

        verify(taskRepository, times(1)).save(any(Task.class));
        verify(activityHistoryService, times(6)).recordActivity(eq(task.getId()), eq(currentUser.getId()), anyString()); // 6 changes: title, desc, status, priority, dueDate, assignee
    }

    @Test
    void deleteTask_shouldDeleteTaskAndRecordActivity() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).deleteById(task.getId());

        taskService.deleteTask(task.getId(), currentUser);

        verify(taskRepository, times(1)).deleteById(task.getId());
        verify(activityHistoryService, times(1)).recordActivity(eq(task.getId()), eq(currentUser.getId()), anyString());
    }

    @Test
    void updateTaskStatus_shouldUpdateStatusAndRecordActivityAndNotifyProjectManager() {
        TaskStatusUpdateRequestDto requestDto = new TaskStatusUpdateRequestDto();
        requestDto.setStatus(TaskStatus.DONE);

        Task updatedTask = Task.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(TaskStatus.DONE)
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .project(project)
                .assignee(assigneeUser)
                .build();

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        taskService.updateTaskStatus(task.getId(), requestDto, currentUser);

        verify(taskRepository, times(1)).save(any(Task.class));
        verify(notificationService, times(1)).createNotification(eq(project.getOwner().getId()), anyString());
        verify(activityHistoryService, times(1)).recordActivity(eq(task.getId()), eq(currentUser.getId()), anyString());
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
    }
}
