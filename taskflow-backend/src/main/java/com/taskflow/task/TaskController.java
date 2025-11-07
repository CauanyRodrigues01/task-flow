package com.taskflow.task;

import com.taskflow.task.dto.TaskRequestDto;
import com.taskflow.task.dto.TaskResponseDto;
import com.taskflow.task.dto.TaskStatusUpdateRequestDto;
import com.taskflow.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@PathVariable Long projectId, @RequestBody TaskRequestDto taskRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        TaskResponseDto createdTask = taskService.createTask(projectId, taskRequestDto, currentUser);
        return ResponseEntity.ok(createdTask);
    }

    @PutMapping("/{taskId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROJECT_MANAGER')")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long taskId, @RequestBody TaskRequestDto taskRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        TaskResponseDto updatedTask = taskService.updateTask(taskId, taskRequestDto, currentUser);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROJECT_MANAGER')")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        taskService.deleteTask(taskId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
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
    public ResponseEntity<TaskResponseDto> updateTaskStatus(@PathVariable Long taskId, @RequestBody TaskStatusUpdateRequestDto statusUpdateRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        TaskResponseDto updatedTask = taskService.updateTaskStatus(taskId, statusUpdateRequestDto, currentUser);
        return ResponseEntity.ok(updatedTask);
    }
}
