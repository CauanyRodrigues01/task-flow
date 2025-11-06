package com.taskflow.project;

import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    private User owner;
    private Project project;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1L)
                .name("Test Owner")
                .email("owner@example.com")
                .build();

        project = Project.builder()
                .id(1L)
                .name("Test Project")
                .description("Project Description")
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createProject_shouldReturnCreatedProject() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project createdProject = projectService.createProject(project, owner.getId());

        assertNotNull(createdProject);
        assertEquals(project.getName(), createdProject.getName());
        verify(userRepository, times(1)).findById(owner.getId());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void createProject_shouldThrowExceptionIfOwnerNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            projectService.createProject(project, 99L);
        });

        assertEquals("Owner not found", exception.getMessage());
        verify(userRepository, times(1)).findById(99L);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void getAllProjects_shouldReturnListOfProjects() {
        List<Project> projects = Arrays.asList(project, new Project());
        when(projectRepository.findAll()).thenReturn(projects);

        List<Project> result = projectService.getAllProjects();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void getProjectById_shouldReturnProjectWhenFound() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        Optional<Project> result = projectService.getProjectById(1L);

        assertTrue(result.isPresent());
        assertEquals(project.getName(), result.get().getName());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void getProjectById_shouldReturnEmptyWhenNotFound() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Project> result = projectService.getProjectById(1L);

        assertFalse(result.isPresent());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void updateProject_shouldReturnUpdatedProject() {
        Project updatedDetails = Project.builder().name("Updated Name").description("Updated Desc").build();
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project result = projectService.updateProject(1L, updatedDetails);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Desc", result.getDescription());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void updateProject_shouldThrowExceptionWhenProjectNotFound() {
        Project updatedDetails = Project.builder().name("Updated Name").description("Updated Desc").build();
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            projectService.updateProject(1L, updatedDetails);
        });

        assertEquals("Project not found", exception.getMessage());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void deleteProject_shouldCallRepositoryDeleteById() {
        doNothing().when(projectRepository).deleteById(anyLong());

        projectService.deleteProject(1L);

        verify(projectRepository, times(1)).deleteById(1L);
    }

    @Test
    void isProjectOwner_shouldReturnTrueWhenUserIsOwner() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        boolean isOwner = projectService.isProjectOwner(1L, owner.getId());

        assertTrue(isOwner);
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void isProjectOwner_shouldReturnFalseWhenUserIsNotOwner() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        boolean isOwner = projectService.isProjectOwner(1L, 99L);

        assertFalse(isOwner);
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void isProjectOwner_shouldReturnFalseWhenProjectNotFound() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        boolean isOwner = projectService.isProjectOwner(1L, owner.getId());

        assertFalse(isOwner);
        verify(projectRepository, times(1)).findById(1L);
    }
}
