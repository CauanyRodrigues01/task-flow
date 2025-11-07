package com.taskflow.activityhistory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityHistoryResponseDto {
    private Long id;
    private Long taskId;
    private Long userId;
    private String username;
    private String description;
    private LocalDateTime createdAt;
}
