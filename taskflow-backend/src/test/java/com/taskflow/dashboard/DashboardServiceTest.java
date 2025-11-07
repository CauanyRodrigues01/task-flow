package com.taskflow.dashboard;

import com.taskflow.dashboard.dto.DashboardResponseDto;
import com.taskflow.project.Project;
import com.taskflow.project.ProjectRepository;
import com.taskflow.task.Task;
import com.taskflow.task.TaskRepository;
import com.taskflow.task.TaskStatus;
import com.taskflow.user.Role;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DashboardService dashboardService;

    private User adminUser;
    private User projectManagerUser;
    private Project project1;
    private Project project2;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        adminUser = User.builder().id(1L).email("admin@example.com").role(Role.ADMIN).build();
        projectManagerUser = User.builder().id(2L).email("pm@example.com").role(Role.MANAGER).build();

        project1 = Project.builder().id(10L).name("Project Alpha").owner(projectManagerUser).build();
        project2 = Project.builder().id(20L).name("Project Beta").owner(adminUser).build();

        task1 = Task.builder().id(100L).title("Task 1").status(TaskStatus.TODO).project(project1).build();
        task2 = Task.builder().id(101L).title("Task 2").status(TaskStatus.IN_PROGRESS).project(project1).build();
        task3 = Task.builder().id(102L).title("Task 3").status(TaskStatus.DONE).project(project2).build();
    }

    @Test
    void getDashboardData_shouldReturnCorrectDataForAdminUser() {
        when(userRepository.findById(adminUser.getId())).thenReturn(Optional.of(adminUser));
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));
        when(taskRepository.countByProjectIdIn(any())).thenReturn(3L);
        when(taskRepository.countTasksByStatusForProjects(any()))
                .thenReturn(Arrays.asList(
                        new Object[]{TaskStatus.TODO, 1L},
                        new Object[]{TaskStatus.IN_PROGRESS, 1L},
                        new Object[]{TaskStatus.DONE, 1L}
                ));

        DashboardResponseDto result = dashboardService.getDashboardData(adminUser.getId(), null);

        assertThat(result).isNotNull();
        assertThat(result.getTotalTasks()).isEqualTo(3L);
        assertThat(result.getTasksByStatus()).containsEntry(TaskStatus.TODO, 1L);
        assertThat(result.getTasksByStatus()).containsEntry(TaskStatus.IN_PROGRESS, 1L);
        assertThat(result.getTasksByStatus()).containsEntry(TaskStatus.DONE, 1L);
        assertThat(result.getProjectProgressSummary().getTotalProjects()).isEqualTo(2L);
        assertThat(result.getProjectProgressSummary().getCompletedPercentage()).isEqualTo(33.33, within(0.01));
    }

    @Test
    void getDashboardData_shouldReturnCorrectDataForProjectManagerUser() {
        when(userRepository.findById(projectManagerUser.getId())).thenReturn(Optional.of(projectManagerUser));
        when(projectRepository.findByOwnerIdOrMembersContaining(projectManagerUser.getId(), projectManagerUser))
                .thenReturn(Collections.singletonList(project1));
        when(taskRepository.countByProjectIdIn(Collections.singletonList(project1.getId()))).thenReturn(2L);
        when(taskRepository.countTasksByStatusForProjects(Collections.singletonList(project1.getId())))
                .thenReturn(Arrays.asList(
                        new Object[]{TaskStatus.TODO, 1L},
                        new Object[]{TaskStatus.IN_PROGRESS, 1L}
                ));

        DashboardResponseDto result = dashboardService.getDashboardData(projectManagerUser.getId(), null);

        assertThat(result).isNotNull();
        assertThat(result.getTotalTasks()).isEqualTo(2L);
        assertThat(result.getTasksByStatus()).containsEntry(TaskStatus.TODO, 1L);
        assertThat(result.getTasksByStatus()).containsEntry(TaskStatus.IN_PROGRESS, 1L);
        assertThat(result.getTasksByStatus()).doesNotContainKey(TaskStatus.DONE);
        assertThat(result.getProjectProgressSummary().getTotalProjects()).isEqualTo(1L);
        assertThat(result.getProjectProgressSummary().getCompletedPercentage()).isEqualTo(0.0);
    }

    @Test
    void getDashboardData_shouldFilterByProjectId() {
        when(userRepository.findById(adminUser.getId())).thenReturn(Optional.of(adminUser));
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));
        when(taskRepository.countByProjectIdIn(Collections.singletonList(project1.getId()))).thenReturn(2L);
        when(taskRepository.countTasksByStatusForProjects(Collections.singletonList(project1.getId())))
                .thenReturn(Arrays.asList(
                        new Object[]{TaskStatus.TODO, 1L},
                        new Object[]{TaskStatus.IN_PROGRESS, 1L}
                ));

        DashboardResponseDto result = dashboardService.getDashboardData(adminUser.getId(), project1.getId());

        assertThat(result).isNotNull();
        assertThat(result.getTotalTasks()).isEqualTo(2L);
        assertThat(result.getTasksByStatus()).containsEntry(TaskStatus.TODO, 1L);
        assertThat(result.getTasksByStatus()).containsEntry(TaskStatus.IN_PROGRESS, 1L);
        assertThat(result.getProjectProgressSummary().getTotalProjects()).isEqualTo(1L);
    }

    @Test
    void getDashboardData_shouldReturnEmptyData_whenNoAccessibleProjects() {
        when(userRepository.findById(projectManagerUser.getId())).thenReturn(Optional.of(projectManagerUser));
        when(projectRepository.findByOwnerIdOrMembersContaining(projectManagerUser.getId(), projectManagerUser))
                .thenReturn(Collections.emptyList());

        DashboardResponseDto result = dashboardService.getDashboardData(projectManagerUser.getId(), null);

        assertThat(result).isNotNull();
        assertThat(result.getTotalTasks()).isEqualTo(0L);
        assertThat(result.getTasksByStatus()).isEmpty();
        assertThat(result.getProjectProgressSummary().getTotalProjects()).isEqualTo(0L);
        assertThat(result.getProjectProgressSummary().getCompletedPercentage()).isEqualTo(0.0);
    }
}
