package com.taskflow.task;

import com.taskflow.project.Project;
import com.taskflow.project.ProjectRepository;
import com.taskflow.user.Role;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MyFocusControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    private User collaborator;
    private User anotherUser;

    @BeforeEach
    void setUp() {
        collaborator = userRepository.save(User.builder()
                .name("Focus User")
                .email("focus@example.com")
                .passwordHash("password")
                .role(Role.COLLABORATOR)
                .build());

        anotherUser = userRepository.save(User.builder()
                .name("Another User")
                .email("another@example.com")
                .passwordHash("password")
                .role(Role.COLLABORATOR)
                .build());

        Project project = projectRepository.save(Project.builder()
                .name("Focus Project")
                .owner(anotherUser)
                .build());

        // ✅ Usando LocalDateTime no dueDate
        taskRepository.save(Task.builder()
                .title("Task 1 - Due Later")
                .project(project)
                .assignee(collaborator)
                .dueDate(LocalDateTime.now().plusDays(5))
                .priority(TaskPriority.LOW)
                .build());

        taskRepository.save(Task.builder()
                .title("Task 2 - Due Sooner")
                .project(project)
                .assignee(collaborator)
                .dueDate(LocalDateTime.now().plusDays(2))
                .priority(TaskPriority.HIGH)
                .build());

        taskRepository.save(Task.builder()
                .title("Task 3 - Due Sooner, Lower Prio")
                .project(project)
                .assignee(collaborator)
                .dueDate(LocalDateTime.now().plusDays(2))
                .priority(TaskPriority.MEDIUM)
                .build());

        // Task de outro usuário
        taskRepository.save(Task.builder()
                .title("Task 4 - Not Mine")
                .project(project)
                .assignee(anotherUser)
                .build());
    }

    @Test
    @WithMockUser(username = "focus@example.com", roles = {"COLLABORATOR"})
    void getMyFocusTasks_shouldReturnOnlyMyTasks_withDefaultSort() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/my-focus")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].title").value("Task 2 - Due Sooner")) // Default: dueDate asc, priority asc
                .andExpect(jsonPath("$[1].title").value("Task 3 - Due Sooner, Lower Prio"))
                .andExpect(jsonPath("$[2].title").value("Task 1 - Due Later"));
    }

    @Test
    @WithMockUser(username = "focus@example.com", roles = {"COLLABORATOR"})
    void getMyFocusTasks_shouldReturnTasksSortedByPriorityDesc() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/my-focus")
                        .param("sortBy", "priority")
                        .param("order", "desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].title").value("Task 2 - Due Sooner")) // HIGH
                .andExpect(jsonPath("$[1].title").value("Task 3 - Due Sooner, Lower Prio")) // MEDIUM
                .andExpect(jsonPath("$[2].title").value("Task 1 - Due Later")); // LOW
    }
}
