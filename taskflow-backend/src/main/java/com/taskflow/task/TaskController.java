package com.taskflow.task;

import com.taskflow.task.dto.TaskRequestDto;
import com.taskflow.task.dto.TaskResponseDto;
import com.taskflow.task.dto.TaskStatusUpdateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/tasks")
@RequiredArgsConstructor
@Tag(name = "Gerenciamento de Tarefas", description = "Endpoints para gerenciar tarefas dentro de um projeto")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROJECT_MANAGER')")
    @Operation(summary = "Cria uma nova tarefa em um projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<TaskResponseDto> createTask(@PathVariable Long projectId, @Valid @RequestBody TaskRequestDto taskRequestDto) {
        TaskResponseDto createdTask = taskService.createTask(projectId, taskRequestDto);
        return ResponseEntity.ok(createdTask);
    }

    @PutMapping("/{taskId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROJECT_MANAGER')")
    @Operation(summary = "Atualiza uma tarefa existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long taskId, @Valid @RequestBody TaskRequestDto taskRequestDto) {
        TaskResponseDto updatedTask = taskService.updateTask(taskId, taskRequestDto);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROJECT_MANAGER')")
    @Operation(summary = "Deleta uma tarefa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarefa deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Busca e filtra tarefas em um projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca de tarefas bem-sucedida"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<List<TaskResponseDto>> getTasksByProjectId(
            @PathVariable Long projectId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) String keyword) {
        List<TaskResponseDto> tasks = taskService.getTasksByProjectId(projectId, status, assigneeId, priority, keyword);
        return ResponseEntity.ok(tasks);
    }

    @PatchMapping("/{taskId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Atualiza o status de uma tarefa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status da tarefa atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Status inválido"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public ResponseEntity<TaskResponseDto> updateTaskStatus(@PathVariable Long taskId, @Valid @RequestBody TaskStatusUpdateRequestDto statusUpdateRequestDto) {
        TaskResponseDto updatedTask = taskService.updateTaskStatus(taskId, statusUpdateRequestDto);
        return ResponseEntity.ok(updatedTask);
    }
}