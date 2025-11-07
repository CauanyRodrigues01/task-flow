package com.taskflow.task;

import com.fasterxml.jackson.databind.ObjectMapper;
<<<<<<< HEAD
import com.taskflow.activityhistory.ActivityHistory;
import com.taskflow.activityhistory.ActivityHistoryRepository;
import com.taskflow.project.Project;
import com.taskflow.project.ProjectRepository;
import com.taskflow.task.dto.TaskStatusUpdateRequestDto;
import com.taskflow.user.Role;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
=======
import com.taskflow.activityhistory.ActivityHistoryRepository;
import com.taskflow.task.dto.TaskRequestDto;
import com.taskflow.task.dto.TaskStatusUpdateRequestDto;
import com.taskflow.project.Project;
import com.taskflow.project.ProjectRepository;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
import com.taskflow.user.Role;
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
<<<<<<< HEAD
import org.springframework.context.annotation.Import;
=======
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
=======
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
<<<<<<< HEAD
    private TaskRepository taskRepository;
=======
    private UserRepository userRepository;
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
<<<<<<< HEAD
    private UserRepository userRepository;
=======
    private TaskRepository taskRepository;
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)

    @Autowired
    private ActivityHistoryRepository activityHistoryRepository;

<<<<<<< HEAD
    private User user;
=======
    private User projectManager;
    private User collaborator;
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
    private Project project;
    private Task task;

    @BeforeEach
    void setUp() {
<<<<<<< HEAD
        user = userRepository.save(User.builder()
                .name("Test User")
                .email("test@example.com")
                .passwordHash("hashedpassword")
                .role(Role.COLLABORATOR)
                .build());

        project = projectRepository.save(Project.builder()
                .name("Test Project")
                .description("Project Description")
                .owner(user)
                .build());

        task = taskRepository.save(Task.builder()
                .title("Test Task")
                .status(TaskStatus.TODO)
                .project(project)
                .build());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void updateTaskStatus_shouldUpdateStatusAndLogActivity_whenNoteIsProvided() throws Exception {
        TaskStatusUpdateRequestDto request = new TaskStatusUpdateRequestDto();
        request.setStatus(TaskStatus.IN_PROGRESS);
        request.setContextNote("Starting work on this task.");

        mockMvc.perform(patch("/api/v1/projects/{projectId}/tasks/{taskId}", project.getId(), task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        Task updatedTask = taskRepository.findById(task.getId()).orElseThrow();
        List<ActivityHistory> history = activityHistoryRepository.findByTaskIdOrderByCreatedAtDesc(task.getId());
        assertThat(history).isNotEmpty();
        assertThat(history.get(0).getDescription()).contains("Nota: Starting work on this task.");
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void updateTaskStatus_shouldUpdateStatusAndLogActivity_whenNoteIsNotProvided() throws Exception {
        TaskStatusUpdateRequestDto request = new TaskStatusUpdateRequestDto();
        request.setStatus(TaskStatus.DONE);

        mockMvc.perform(patch("/api/v1/projects/{projectId}/tasks/{taskId}", project.getId(), task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"));

        Task updatedTask = taskRepository.findById(task.getId()).orElseThrow();
        assertThat(updatedTask.getStatus()).isEqualTo(TaskStatus.DONE);

        List<ActivityHistory> history = activityHistoryRepository.findByTaskIdOrderByCreatedAtDesc(task.getId());
        assertThat(history).isNotEmpty();
        assertThat(history.get(0).getDescription()).doesNotContain("Nota:");
        assertThat(history.get(0).getDescription()).contains("atualizado de 'TODO' para 'DONE'");
=======
        activityHistoryRepository.deleteAll();
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();

        projectManager = User.builder()
                .name("Project Manager")
                .email("pm@example.com")
                .passwordHash("password")
                .role(Role.PROJECT_MANAGER)
                .build();
        userRepository.save(projectManager);

        collaborator = User.builder()
                .name("Collaborator User")
                .email("collab@example.com")
                .passwordHash("password")
                .role(Role.COLLABORATOR)
                .build();
        userRepository.save(collaborator);

        project = Project.builder()
                .name("Integration Project")
                .description("Project for integration tests")
                .owner(projectManager)
                .members(new HashSet<>(Set.of(projectManager, collaborator)))
                .createdAt(LocalDateTime.now())
                .build();
        projectRepository.save(project);

        task = Task.builder()
                .title("Integration Task")
                .description("Task for integration tests")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.MEDIUM)
                .project(project)
                .assignee(collaborator)
                .build();
        taskRepository.save(task);
    }

    @Test
    @WithMockUser(username = "pm@example.com", roles = {"PROJECT_MANAGER"})
    void updateTaskStatus_shouldUpdateStatusAndRecordActivityWithoutContextNote() throws Exception {
        TaskStatusUpdateRequestDto requestDto = new TaskStatusUpdateRequestDto();
        requestDto.setStatus(TaskStatus.IN_PROGRESS);

        mockMvc.perform(patch("/api/v1/projects/{projectId}/tasks/{taskId}", project.getId(), task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")));

        // Verify activity history
        assertEquals(1, activityHistoryRepository.findAll().size());
        String expectedDescription = String.format("Status da tarefa '%s' alterado de TODO para IN_PROGRESS", task.getTitle());
        assertEquals(expectedDescription, activityHistoryRepository.findAll().get(0).getDescription());
    }

    @Test
    @WithMockUser(username = "pm@example.com", roles = {"PROJECT_MANAGER"})
    void updateTaskStatus_shouldUpdateStatusAndRecordActivityWithContextNote() throws Exception {
        String contextNote = "Iniciei a implementação da API.";
        TaskStatusUpdateRequestDto requestDto = new TaskStatusUpdateRequestDto();
        requestDto.setStatus(TaskStatus.IN_PROGRESS);
        requestDto.setContextNote(contextNote);

        mockMvc.perform(patch("/api/v1/projects/{projectId}/tasks/{taskId}", project.getId(), task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")));

        // Verify activity history
        assertEquals(1, activityHistoryRepository.findAll().size());
        String expectedDescription = String.format("Status da tarefa '%s' alterado de TODO para IN_PROGRESS. Nota: %s", task.getTitle(), contextNote);
        assertEquals(expectedDescription, activityHistoryRepository.findAll().get(0).getDescription());
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
    }
}
