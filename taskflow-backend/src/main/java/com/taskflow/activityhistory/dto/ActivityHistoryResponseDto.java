package com.taskflow.activityhistory.dto;

<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
=======
import lombok.Data;
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)

import java.time.LocalDateTime;

@Data
<<<<<<< HEAD
@Builder
@NoArgsConstructor
@AllArgsConstructor
=======
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
public class ActivityHistoryResponseDto {
    private Long id;
    private Long taskId;
    private Long userId;
<<<<<<< HEAD
    private String username;
=======
    private String userName;
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
    private String description;
    private LocalDateTime createdAt;
}
