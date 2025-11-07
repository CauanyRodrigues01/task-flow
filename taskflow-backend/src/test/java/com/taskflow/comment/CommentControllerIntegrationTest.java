package com.taskflow.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.comment.dto.CommentRequestDto;
import com.taskflow.project.Project;
import com.taskflow.project.ProjectRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
=======
import java.time.LocalDateTime;
import java.util.HashSet;
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
<<<<<<< HEAD
=======
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)

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
<<<<<<< HEAD
=======
    private UserRepository userRepository;

    @Autowired
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

<<<<<<< HEAD
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
=======
    private User projectManager;
    private User collaborator;
    private User nonMember;
    private Project project;
    private Task task;

    @BeforeEach
    void setUp() {
        // Clear repositories to ensure a clean state for each test
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

        nonMember = User.builder()
                .name("Non Member")
                .email("nonmember@example.com")
                .passwordHash("password")
                .role(Role.VIEWER)
                .build();
        userRepository.save(nonMember);

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
                .project(project)
                .build();
        taskRepository.save(task);
    }

    @Test
    @WithMockUser(username = "pm@example.com", roles = {"PROJECT_MANAGER"})
    void createComment_shouldReturnCreatedComment_whenProjectManagerComments() throws Exception {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("This is a comment from PM.");

        mockMvc.perform(post("/api/v1/tasks/{taskId}/comments", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.content", is("This is a comment from PM.")))
                .andExpect(jsonPath("$.taskId", is(task.getId().intValue())))
                .andExpect(jsonPath("$.authorName", is(projectManager.getName())));
    }

    @Test
    @WithMockUser(username = "collab@example.com", roles = {"COLLABORATOR"})
    void createComment_shouldReturnCreatedComment_whenCollaboratorComments() throws Exception {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("This is a comment from Collaborator.");

        mockMvc.perform(post("/api/v1/tasks/{taskId}/comments", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.content", is("This is a comment from Collaborator.")))
                .andExpect(jsonPath("$.taskId", is(task.getId().intValue())))
                .andExpect(jsonPath("$.authorName", is(collaborator.getName())));
    }

    @Test
    @WithMockUser(username = "nonmember@example.com", roles = {"VIEWER"})
    void createComment_shouldReturnForbidden_whenUserIsNotProjectMember() throws Exception {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("This is a comment from non-member.");

        mockMvc.perform(post("/api/v1/tasks/{taskId}/comments", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
                .andExpect(status().isForbidden());
    }

    @Test
<<<<<<< HEAD
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
=======
    @WithMockUser(username = "pm@example.com", roles = {"PROJECT_MANAGER"})
    void createComment_shouldReturnNotFound_whenTaskDoesNotExist() throws Exception {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("Comment for non-existent task.");

        mockMvc.perform(post("/api/v1/tasks/{taskId}/comments", 999L) // Non-existent task ID
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "pm@example.com", roles = {"PROJECT_MANAGER"})
    void createComment_shouldReturnBadRequest_whenContentIsEmpty() throws Exception {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent(""); // Empty content

        mockMvc.perform(post("/api/v1/tasks/{taskId}/comments", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
