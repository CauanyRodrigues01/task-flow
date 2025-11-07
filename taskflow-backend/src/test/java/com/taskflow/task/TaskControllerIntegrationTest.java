package com.taskflow.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.activityhistory.ActivityHistory;
import com.taskflow.activityhistory.ActivityHistoryRepository;
import com.taskflow.project.Project;
import com.taskflow.project.ProjectRepository;
import com.taskflow.task.dto.TaskStatusUpdateRequestDto;
import com.taskflow.user.Role;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityHistoryRepository activityHistoryRepository;

    private User user;
    private Project project;
    private Task task;

    @BeforeEach
    void setUp() {
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
    }
}
