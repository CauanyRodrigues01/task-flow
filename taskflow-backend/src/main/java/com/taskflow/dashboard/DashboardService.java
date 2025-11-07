package com.taskflow.dashboard;

import com.taskflow.dashboard.dto.DashboardResponseDto;
import com.taskflow.project.Project;
import com.taskflow.project.ProjectRepository;
import com.taskflow.task.TaskRepository;
import com.taskflow.task.TaskStatus;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public DashboardResponseDto getDashboardData(Long userId, Long projectId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Project> accessibleProjects;
        if (currentUser.isAdmin()) {
            accessibleProjects = projectRepository.findAll();
        } else {
            accessibleProjects = projectRepository.findByOwnerIdOrMembersContaining(userId, currentUser);
        }

        if (projectId != null) {
            accessibleProjects = accessibleProjects.stream()
                    .filter(p -> p.getId().equals(projectId))
                    .collect(Collectors.toList());
        }

        List<Long> accessibleProjectIds = accessibleProjects.stream()
                .map(Project::getId)
                .collect(Collectors.toList());

        if (accessibleProjectIds.isEmpty()) {
            return DashboardResponseDto.builder()
                    .totalTasks(0L)
                    .tasksByStatus(Map.of())
                    .projectProgressSummary(DashboardResponseDto.ProjectProgressSummary.builder()
                            .completedPercentage(0.0)
                            .totalProjects(0L)
                            .build())
                    .build();
        }

        Long totalTasks = taskRepository.countByProjectIdIn(accessibleProjectIds);
        Map<TaskStatus, Long> tasksByStatus = taskRepository.countTasksByStatusForProjects(accessibleProjectIds).stream()
                .collect(Collectors.toMap(o -> (TaskStatus) o[0], o -> (Long) o[1]));

        long totalCompletedTasks = tasksByStatus.getOrDefault(TaskStatus.DONE, 0L);
        double completedPercentage = totalTasks > 0 ? (double) totalCompletedTasks / totalTasks * 100 : 0.0;

        return DashboardResponseDto.builder()
                .totalTasks(totalTasks)
                .tasksByStatus(tasksByStatus)
                .projectProgressSummary(DashboardResponseDto.ProjectProgressSummary.builder()
                        .completedPercentage(completedPercentage)
                        .totalProjects((long) accessibleProjects.size())
                        .build())
                .build();
    }
}
