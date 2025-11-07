package com.taskflow.activityhistory;

import com.taskflow.project.Project;
import com.taskflow.project.ProjectRepository;
import com.taskflow.task.Task;
import com.taskflow.task.TaskRepository;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ActivityHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "test@example.com")
    void getTaskHistory_shouldReturnOk() throws Exception {
        // Given
        User user = userRepository.save(
                User.builder()
                        .email("test@example.com")
                        .passwordHash("password")
                        .name("Test User")
                        .build()
        );

        Project project = projectRepository.save(
                Project.builder()
                        .name("Test Project")
                        .owner(user)
                        .members(Set.of(user))
                        .build()
        );

        Task task = taskRepository.save(
                Task.builder()
                        .title("Test Task")
                        .project(project)
                        .build()
        );

        // When & Then
        mockMvc.perform(get("/tasks/{taskId}/history", task.getId()))
                .andExpect(status().isOk());
    }
}
