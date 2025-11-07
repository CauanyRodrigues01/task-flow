package com.taskflow.task.dto;

import com.taskflow.task.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskStatusUpdateRequestDto {
    @NotNull(message = "O status não pode ser nulo")
    private TaskStatus status;
}
