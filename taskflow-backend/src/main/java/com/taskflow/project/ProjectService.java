package com.taskflow.project;

import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository; // Assuming a UserRepository to fetch User details

    @PreAuthorize("hasAnyRole('PROJECT_MANAGER', 'ADMIN')")
    public Project createProject(Project project, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        project.setOwner(owner);
        return projectRepository.save(project);
    }

    @PreAuthorize("hasAnyRole('PROJECT_MANAGER', 'ADMIN')")
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('PROJECT_MANAGER', 'ADMIN')")
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    @PreAuthorize("hasAnyRole('PROJECT_MANAGER', 'ADMIN') and @projectService.isProjectOwner(#id, authentication.principal.id)")
    public Project updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        project.setName(projectDetails.getName());
        project.setDescription(projectDetails.getDescription());

        return projectRepository.save(project);
    }

    @PreAuthorize("hasAnyRole('PROJECT_MANAGER', 'ADMIN') and @projectService.isProjectOwner(#id, authentication.principal.id)")
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    // Helper method for @PreAuthorize to check if the authenticated user is the owner of the project
    public boolean isProjectOwner(Long projectId, Long userId) {
        return projectRepository.findById(projectId)
                .map(project -> project.getOwner().getId().equals(userId))
                .orElse(false);
    }

    @PreAuthorize("hasAnyRole('PROJECT_MANAGER', 'ADMIN')")
    public java.util.Set<com.taskflow.user.User> getProjectMembers(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return project.getMembers();
    }

    @PreAuthorize("hasAnyRole('PROJECT_MANAGER', 'ADMIN') and @projectService.isProjectOwner(#projectId, authentication.principal.id)")
    public void addMember(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        project.getMembers().add(user);
        projectRepository.save(project);
    }

    @PreAuthorize("hasAnyRole('PROJECT_MANAGER', 'ADMIN') and @projectService.isProjectOwner(#projectId, authentication.principal.id)")
    public void removeMember(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        project.getMembers().remove(user);
        projectRepository.save(project);
    }
}
