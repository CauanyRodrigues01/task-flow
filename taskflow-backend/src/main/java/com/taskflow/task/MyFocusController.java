package com.taskflow.task;

import com.taskflow.task.dto.TaskResponseDto;
import com.taskflow.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class MyFocusController {

    private final TaskService taskService;

    @GetMapping("/my-focus")
    @PreAuthorize("hasAnyAuthority('COLLABORATOR', 'MANAGER', 'ADMIN')")
    public ResponseEntity<List<TaskResponseDto>> getMyFocusTasks(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false, defaultValue = "dueDate") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order) {
        List<TaskResponseDto> tasks = taskService.getTasksByAssigneeId(currentUser.getId(), sortBy, order);
        return ResponseEntity.ok(tasks);
    }
}
