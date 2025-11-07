package com.taskflow.dashboard.dto;

import com.taskflow.task.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class DashboardResponseDto {
    private Long totalTasks;
    private Map<TaskStatus, Long> tasksByStatus;
    private ProjectProgressSummary projectProgressSummary;

    @Data
    @Builder
    public static class ProjectProgressSummary {
        private Double completedPercentage;
        private Long totalProjects;
    }
}
