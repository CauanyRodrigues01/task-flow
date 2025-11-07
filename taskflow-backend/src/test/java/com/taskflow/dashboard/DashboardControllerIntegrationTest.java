package com.taskflow.dashboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.TestDatabaseConfig;
import com.taskflow.project.Project;
import com.taskflow.project.ProjectRepository;
import com.taskflow.task.Task;
import com.taskflow.task.TaskRepository;
import com.taskflow.task.TaskStatus;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class DashboardControllerIntegrationTest {

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

    private User adminUser;
    private User projectManagerUser;
    private Project project1;
    private Project project2;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        adminUser = userRepository.save(User.builder()
                .name("Admin User")
                .email("admin@example.com")
                .passwordHash("password")
                .role(Role.ADMIN)
                .build());

        projectManagerUser = userRepository.save(User.builder()
                .name("PM User")
                .email("pm@example.com")
                .passwordHash("password")
                .role(Role.MANAGER)
                .build());

        project1 = projectRepository.save(Project.builder()
                .name("Project Alpha")
                .description("Description Alpha")
                .owner(projectManagerUser)
                .build());

        project2 = projectRepository.save(Project.builder()
                .name("Project Beta")
                .description("Description Beta")
                .owner(adminUser)
                .build());

        task1 = taskRepository.save(Task.builder()
                .title("Task 1")
                .status(TaskStatus.TODO)
                .project(project1)
                .assignee(projectManagerUser)
                .build());

        task2 = taskRepository.save(Task.builder()
                .title("Task 2")
                .status(TaskStatus.IN_PROGRESS)
                .project(project1)
                .assignee(projectManagerUser)
                .build());

        task3 = taskRepository.save(Task.builder()
                .title("Task 3")
                .status(TaskStatus.DONE)
                .project(project2)
                .assignee(adminUser)
                .build());
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void getDashboardData_shouldReturnDataForAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTasks").value(3))
                .andExpect(jsonPath("$.tasksByStatus.TODO").value(1))
                .andExpect(jsonPath("$.tasksByStatus.IN_PROGRESS").value(1))
                .andExpect(jsonPath("$.tasksByStatus.DONE").value(1))
                .andExpect(jsonPath("$.projectProgressSummary.totalProjects").value(2));
    }

    @Test
    @WithMockUser(username = "pm@example.com", roles = {"MANAGER"})
    void getDashboardData_shouldReturnDataForProjectManager() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTasks").value(2))
                .andExpect(jsonPath("$.tasksByStatus.TODO").value(1))
                .andExpect(jsonPath("$.tasksByStatus.IN_PROGRESS").value(1))
                .andExpect(jsonPath("$.tasksByStatus.DONE").doesNotExist())
                .andExpect(jsonPath("$.projectProgressSummary.totalProjects").value(1));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void getDashboardData_shouldFilterByProjectId() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard")
                        .param("projectId", project1.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTasks").value(2))
                .andExpect(jsonPath("$.tasksByStatus.TODO").value(1))
                .andExpect(jsonPath("$.tasksByStatus.IN_PROGRESS").value(1))
                .andExpect(jsonPath("$.tasksByStatus.DONE").doesNotExist())
                .andExpect(jsonPath("$.projectProgressSummary.totalProjects").value(1));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"COLLABORATOR"})
    void getDashboardData_shouldReturnForbiddenForUnauthorizedUser() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
