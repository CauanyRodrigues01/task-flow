package com.taskflow.task.dto;

import com.taskflow.task.TaskPriority;
import com.taskflow.task.TaskStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequestDto {
    @NotBlank(message = "O título não pode estar em branco")
    @Size(max = 100, message = "O título não pode ter mais de 100 caracteres")
    private String title;

    @Size(max = 500, message = "A descrição não pode ter mais de 500 caracteres")
    private String description;

    @NotNull(message = "O status não pode ser nulo")
    private TaskStatus status;

    @NotNull(message = "A prioridade não pode ser nula")
    private TaskPriority priority;

    @Future(message = "A data de vencimento deve ser no futuro")
    private LocalDateTime dueDate;

    @NotNull(message = "O ID do responsável não pode ser nulo")
    private Long assigneeId;
}
