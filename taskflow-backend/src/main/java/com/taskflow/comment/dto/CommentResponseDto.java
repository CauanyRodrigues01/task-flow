package com.taskflow.comment.dto;

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
public class CommentResponseDto {
    private Long id;
    private String content;
    private Long taskId;
    private Long authorId;
    private String authorName;
    private LocalDateTime createdAt;
}
