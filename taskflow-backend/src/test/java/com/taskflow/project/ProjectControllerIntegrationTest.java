package com.taskflow.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.project.dto.ProjectRequest;
import com.taskflow.user.Role;
import com.taskflow.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectService projectService;

    private User projectManager;
    private Project project;

    @BeforeEach
    void setUp() {
        projectManager = User.builder()
                .id(1L)
                .name("Project Manager")
                .email("pm@example.com")
                .role(Role.MANAGER)
                .build();

        project = Project.builder()
                .id(1L)
                .name("Test Project")
                .description("Project Description")
                .owner(projectManager)
                .createdAt(LocalDateTime.now())
                .build();

        // Set up security context for authenticated user
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(projectManager, null, projectManager.getAuthorities())
        );
    }

    @Test
    void createProject_shouldReturnCreatedProject() throws Exception {
        ProjectRequest request = ProjectRequest.builder()
                .name("New Project")
                .description("New Project Description")
                .build();

        Project newProject = Project.builder()
                .id(2L)
                .name(request.getName())
                .description(request.getDescription())
                .owner(projectManager)
                .createdAt(LocalDateTime.now())
                .build();

        when(projectService.createProject(any(Project.class), anyLong())).thenReturn(newProject);

        mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.description").value(request.getDescription()));
    }

    @Test
    void getAllProjects_shouldReturnListOfProjects() throws Exception {
        when(projectService.getAllProjects()).thenReturn(Arrays.asList(project));

        mockMvc.perform(get("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(project.getName()));
    }

    @Test
    void getProjectById_shouldReturnProjectWhenFound() throws Exception {
        when(projectService.getProjectById(anyLong())).thenReturn(Optional.of(project));

        mockMvc.perform(get("/api/v1/projects/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(project.getName()));
    }

    @Test
    void getProjectById_shouldReturnNotFoundWhenNotFound() throws Exception {
        when(projectService.getProjectById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/projects/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProject_shouldReturnUpdatedProject() throws Exception {
        ProjectRequest request = ProjectRequest.builder()
                .name("Updated Project Name")
                .description("Updated Project Description")
                .build();

        Project updatedProject = Project.builder()
                .id(1L)
                .name(request.getName())
                .description(request.getDescription())
                .owner(projectManager)
                .createdAt(LocalDateTime.now())
                .build();

        when(projectService.updateProject(anyLong(), any(Project.class))).thenReturn(updatedProject);
        when(projectService.isProjectOwner(anyLong(), anyLong())).thenReturn(true); // Mock for @PreAuthorize

        mockMvc.perform(put("/api/v1/projects/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.getName()));
    }

    @Test
    void deleteProject_shouldReturnNoContent() throws Exception {
        doNothing().when(projectService).deleteProject(anyLong());
        when(projectService.isProjectOwner(anyLong(), anyLong())).thenReturn(true); // Mock for @PreAuthorize

        mockMvc.perform(delete("/api/v1/projects/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
