package com.taskflow.activityhistory;

import com.taskflow.activityhistory.dto.ActivityHistoryResponseDto;
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
@RequiredArgsConstructor
public class ActivityHistoryController {

    private final ActivityHistoryService activityHistoryService;
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
    }
}
