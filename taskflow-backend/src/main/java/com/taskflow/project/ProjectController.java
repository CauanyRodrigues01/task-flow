package com.taskflow.project;

import com.taskflow.project.dto.ProjectRequest;
import com.taskflow.project.dto.ProjectResponse;
import com.taskflow.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Gerenciamento de Projetos", description = "Endpoints para criar, listar, atualizar e deletar projetos, além de gerenciar membros")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @Operation(summary = "Cria um novo projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Projeto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    })
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest projectRequest) {
        // Temporarily hardcoded owner ID as security is disabled for debugging
        Long ownerId = 1L;

        Project project = Project.builder()
                .name(projectRequest.getName())
                .description(projectRequest.getDescription())
                .build();

        Project createdProject = projectService.createProject(project, ownerId);
        return new ResponseEntity<>(convertToDto(createdProject), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Lista todos os projetos")
    @ApiResponse(responseCode = "200", description = "Projetos listados com sucesso")
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um projeto pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto encontrado"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(project -> ResponseEntity.ok(convertToDto(project)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um projeto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectRequest projectRequest) {
        Project projectDetails = Project.builder()
                .name(projectRequest.getName())
                .description(projectRequest.getDescription())
                .build();
        Project updatedProject = projectService.updateProject(id, projectDetails);
        return ResponseEntity.ok(convertToDto(updatedProject));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Projeto deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{projectId}/members")
    @Operation(summary = "Lista os membros de um projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membros listados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<java.util.Set<com.taskflow.user.User>> getProjectMembers(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProjectMembers(projectId));
    }

    @PostMapping("/{projectId}/members")
    @Operation(summary = "Adiciona um membro a um projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membro adicionado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto ou usuário não encontrado")
    })
    public ResponseEntity<Void> addMemberToProject(@PathVariable Long projectId, @RequestBody Long userId) {
        projectService.addMember(projectId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{projectId}/members/{userId}")
    @Operation(summary = "Remove um membro de um projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membro removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto ou usuário não encontrado")
    })
    public ResponseEntity<Void> removeMemberFromProject(@PathVariable Long projectId, @PathVariable Long userId) {
        projectService.removeMember(projectId, userId);
        return ResponseEntity.ok().build();
    }

    private ProjectResponse convertToDto(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .ownerId(project.getOwner() != null ? project.getOwner().getId() : null)
                .ownerName(project.getOwner() != null ? project.getOwner().getName() : null)
                .createdAt(project.getCreatedAt())
                .build();
    }
}
