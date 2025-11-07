package com.taskflow.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequestDto {
<<<<<<< HEAD
    @NotBlank(message = "Content cannot be blank")
=======
    @NotBlank(message = "O conteúdo do comentário não pode ser vazio.")
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
    private String content;
}
