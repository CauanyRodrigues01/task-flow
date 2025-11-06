package com.taskflow.task.dto;

import com.taskflow.task.TaskStatus;
import lombok.Data;

@Data
public class TaskStatusUpdateRequestDto {
    private TaskStatus status;
}
