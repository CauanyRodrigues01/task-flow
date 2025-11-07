package com.taskflow.activityhistory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.project.Project;
import com.taskflow.project.ProjectRepository;
import com.taskflow.task.Task;
import com.taskflow.task.TaskRepository;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
import com.taskflow.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ActivityHistoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ActivityHistoryRepository activityHistoryRepository;

    private User testUser;
    private User adminUser;
    private Project project;
    private Task task;

    @BeforeEach
    void setUp() {
        activityHistoryRepository.deleteAll();
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();

        testUser = User.builder()
                .name("Test User")
                .email("test@example.com")
                .passwordHash("password")
                .role(Role.COLLABORATOR)
                .build();
        userRepository.save(testUser);

        adminUser = User.builder()
                .name("Admin User")
                .email("admin@example.com")
                .passwordHash("password")
                .role(Role.ADMIN)
                .build();
        userRepository.save(adminUser);

        project = Project.builder()
                .name("Integration Project")
                .description("Project for integration tests")
                .owner(testUser)
                .members(new HashSet<>(Set.of(testUser)))
                .createdAt(LocalDateTime.now())
                .build();
        projectRepository.save(project);

        task = Task.builder()
                .title("Integration Task")
                .description("Task for integration tests")
                .project(project)
                .build();
        taskRepository.save(task);

        activityHistoryRepository.save(ActivityHistory.builder()
                .task(task)
                .user(testUser)
                .description("Task created")
                .createdAt(LocalDateTime.now().minusHours(1))
                .build());
        activityHistoryRepository.save(ActivityHistory.builder()
                .task(task)
                .user(testUser)
                .description("Status changed")
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"COLLABORATOR"})
    void getTaskHistory_shouldReturnHistoryForAuthorizedUser() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/{taskId}/history", task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].description", is("Task created")));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void getTaskHistory_shouldReturnHistoryForAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/{taskId}/history", task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "other@example.com", roles = {"VIEWER"})
    void getTaskHistory_shouldReturnForbiddenForUnauthorizedUser() throws Exception {
        // Assuming 'other@example.com' is not a member of the project
        mockMvc.perform(get("/api/v1/tasks/{taskId}/history", task.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = {"COLLABORATOR"})
    void getTaskHistory_shouldReturnNotFound_whenTaskDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/{taskId}/history", 999L))
                .andExpect(status().isNotFound());
    }
}
