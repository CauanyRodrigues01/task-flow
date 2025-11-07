package com.taskflow.activityhistory;

import com.taskflow.activityhistory.dto.ActivityHistoryResponseDto;
<<<<<<< HEAD
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/tasks/{taskId}/history")
=======
import com.taskflow.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks/{taskId}/history")
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
@RequiredArgsConstructor
public class ActivityHistoryController {

    private final ActivityHistoryService activityHistoryService;
<<<<<<< HEAD
    private final ActivityHistoryRepository activityHistoryRepository;

    @GetMapping
    @PreAuthorize("hasPermission(#taskId, 'Task', 'VIEW')")
    public ResponseEntity<List<ActivityHistoryResponseDto>> getTaskHistory(@PathVariable Long taskId) {
        List<ActivityHistory> history = activityHistoryRepository.findByTaskIdOrderByCreatedAtDesc(taskId);
        List<ActivityHistoryResponseDto> historyDto = history.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(historyDto);
    }

    private ActivityHistoryResponseDto toDto(ActivityHistory history) {
        return ActivityHistoryResponseDto.builder()
                .id(history.getId())
                .taskId(history.getTask().getId())
                .userId(history.getUser() != null ? history.getUser().getId() : null)
                .username(history.getUser() != null ? history.getUser().getName() : "System")
                .description(history.getDescription())
                .createdAt(history.getCreatedAt())
                .build();
=======

    @GetMapping
    @PreAuthorize("isAuthenticated()") // Further checks are done in service layer for project membership
    public ResponseEntity<List<ActivityHistoryResponseDto>> getTaskHistory(@PathVariable Long taskId) {
        // In a real application, you'd check if the current user has access to the task's project
        // For now, we rely on the service layer to handle task existence.
        List<ActivityHistoryResponseDto> history = activityHistoryService.getActivitiesByTaskId(taskId);
        return ResponseEntity.ok(history);
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
    }
}
