package com.taskflow.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.comment.dto.CommentRequestDto;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CommentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @WithMockUser(username = "test@example.com")
    @Transactional
    void createComment_whenAuthenticatedAndMember_shouldReturnCreated() throws Exception {
        User user = userRepository.save(User.builder().email("test@example.com").passwordHash("password").name("Test User").build());
        Project project = projectRepository.save(Project.builder().name("Test Project").owner(user).members(Set.of(user)).build());
        Task task = taskRepository.save(Task.builder().title("Test Task").project(project).build());

        CommentRequestDto commentRequest = new CommentRequestDto();
        commentRequest.setContent("This is a test comment");

        mockMvc.perform(post("/tasks/{taskId}/comments", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("This is a test comment"))
                .andExpect(jsonPath("$.authorName").value("Test User"));
    }

    @Test
    @WithMockUser(username = "nonmember@example.com")
    @Transactional
    void createComment_whenAuthenticatedAndNotMember_shouldReturnForbidden() throws Exception {
        User user = userRepository.save(User.builder().email("test@example.com").passwordHash("password").name("Test User").build());
        User nonMember = userRepository.save(User.builder().email("nonmember@example.com").passwordHash("password").build());
        Project project = projectRepository.save(Project.builder().name("Test Project").owner(user).members(Set.of(user)).build());
        Task task = taskRepository.save(Task.builder().title("Test Task").project(project).build());

        CommentRequestDto commentRequest = new CommentRequestDto();
        commentRequest.setContent("This is a test comment");

        mockMvc.perform(post("/tasks/{taskId}/comments", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void createComment_whenNotAuthenticated_shouldReturnUnauthorized() throws Exception {
        User user = userRepository.save(User.builder().email("test@example.com").passwordHash("password").name("Test User").build());
        Project project = projectRepository.save(Project.builder().name("Test Project").owner(user).members(Set.of(user)).build());
        Task task = taskRepository.save(Task.builder().title("Test Task").project(project).build());

        CommentRequestDto commentRequest = new CommentRequestDto();
        commentRequest.setContent("This is a test comment");

        mockMvc.perform(post("/tasks/{taskId}/comments", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isUnauthorized());
    }
}