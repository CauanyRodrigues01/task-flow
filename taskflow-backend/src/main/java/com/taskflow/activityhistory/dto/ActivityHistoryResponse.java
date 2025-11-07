package com.taskflow.activityhistory.dto;

import java.time.LocalDateTime;

public record ActivityHistoryResponse(
        Long id,
        Long taskId,
        Long userId,
        String description,
        LocalDateTime timestamp
) {
}
