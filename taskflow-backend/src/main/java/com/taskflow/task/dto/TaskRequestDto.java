package com.taskflow.task.dto;

import com.taskflow.task.TaskPriority;
import com.taskflow.task.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequestDto {
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime dueDate;
    private Long assigneeId;
    private String contextNote;
}
