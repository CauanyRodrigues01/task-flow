package com.taskflow.notification.dto;

<<<<<<< HEAD
import lombok.Builder;
=======
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
import lombok.Data;

import java.time.LocalDateTime;

@Data
<<<<<<< HEAD
@Builder
=======
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
public class NotificationResponseDto {
    private Long id;
    private Long userId;
    private String message;
    private Boolean readStatus;
    private LocalDateTime createdAt;
}
