package com.taskflow.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.project.dto.ProjectRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.taskflow.TestDatabaseConfig;
import org.springframework.context.annotation.Import;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Import(TestDatabaseConfig.class)
class ProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    private User projectManager;
    private Project project;

    @BeforeEach
    void setUp() {
        projectManager = userRepository.save(User.builder()
                .name("Project Manager")
                .email("pm@example.com")
                .passwordHash("hashedpassword")
                .role(Role.ADMIN)
                .build());

        project = projectRepository.save(Project.builder()
                .name("Test Project")
                .description("Project Description")
                .owner(projectManager)
                .build());
    }

    @Test
    @WithMockUser(username = "pm@example.com")
    void createProject_shouldReturnCreatedProject() throws Exception {
        ProjectRequest request = ProjectRequest.builder()
                .name("New Project")
                .description("New Project Description")
                .build();

        mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.description").value(request.getDescription()));
    }

    @Test
    @WithMockUser(username = "pm@example.com")
    void getAllProjects_shouldReturnListOfProjects() throws Exception {
        mockMvc.perform(get("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(project.getName()));
    }

    @Test
    @WithMockUser(username = "pm@example.com")
    void getProjectById_shouldReturnProjectWhenFound() throws Exception {
        mockMvc.perform(get("/api/v1/projects/{id}", project.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(project.getName()));
    }

    @Test
    @WithMockUser(username = "pm@example.com")
    void getProjectById_shouldReturnNotFoundWhenNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/projects/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "pm@example.com")
    void updateProject_shouldReturnUpdatedProject() throws Exception {
        ProjectRequest request = ProjectRequest.builder()
                .name("Updated Project Name")
                .description("Updated Project Description")
                .build();

        mockMvc.perform(put("/api/v1/projects/{id}", project.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.getName()));
    }

    @Test
    @WithMockUser(username = "pm@example.com")
    void deleteProject_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/projects/{id}", project.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void createProject_shouldReturnUnauthorized_whenNotAuthenticated() throws Exception {
        ProjectRequest request = ProjectRequest.builder()
                .name("Unauthorized Project")
                .description("Should fail")
                .build();

        mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "pm@example.com")
    void addMemberToProject_shouldReturnOk() throws Exception {
        User member = userRepository.save(User.builder().email("member@example.com").build());
        mockMvc.perform(post("/api/v1/projects/{projectId}/members", project.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(member.getId().toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "pm@example.com")
    void removeMemberFromProject_shouldReturnOk() throws Exception {
        User member = userRepository.save(User.builder().email("member@example.com").build());
        project.getMembers().add(member);
        projectRepository.save(project);

        mockMvc.perform(delete("/api/v1/projects/{projectId}/members/{userId}", project.getId(), member.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "pm@example.com")
    void getProjectMembers_shouldReturnListOfMembers() throws Exception {
        User member = userRepository.save(User.builder().name("Test Member").email("member@example.com").role(Role.COLLABORATOR).build());
        project.getMembers().add(member);
        projectRepository.save(project);

        mockMvc.perform(get("/api/v1/projects/{projectId}/members", project.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(projectManager.getName()));
    }
}

