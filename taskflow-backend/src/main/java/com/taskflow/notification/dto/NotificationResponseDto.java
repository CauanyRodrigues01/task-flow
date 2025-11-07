package com.taskflow.notification.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponseDto {
    private Long id;
    private Long userId;
    private String message;
    private Boolean readStatus;
    private LocalDateTime createdAt;
}
