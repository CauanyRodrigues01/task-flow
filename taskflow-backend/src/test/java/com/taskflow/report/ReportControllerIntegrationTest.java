package com.taskflow.report;

import com.taskflow.project.Project;
import com.taskflow.project.ProjectRepository;
import com.taskflow.task.Task;
import com.taskflow.task.TaskPriority;
import com.taskflow.task.TaskRepository;
import com.taskflow.task.TaskStatus;
import com.taskflow.user.Role;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ReportControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    private User projectManager;
    private Project project;
    private Task task;

    @BeforeEach
    void setUp() {
        projectManager = userRepository.save(User.builder()
                .name("Test PM")
                .email("pm.report@example.com")
                .passwordHash("password")
                .role(Role.MANAGER)
                .build());

        project = projectRepository.save(Project.builder()
                .name("Report Project")
                .owner(projectManager)
                .build());

        task = taskRepository.save(Task.builder()
                .title("Test Report Task")
                .description("A task for the report")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.HIGH)
                .project(project)
                .assignee(projectManager)
                .build());
    }

    @Test
    @WithMockUser(username = "pm.report@example.com", roles = {"MANAGER"})
    void generateTaskReport_shouldReturnCsvFile() throws Exception {
        mockMvc.perform(get("/api/v1/reports/tasks")
                        .param("projectId", project.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"tasks_report_project_" + project.getId() + ".csv\""))
                .andExpect(content().contentType("text/csv"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("ID,Title,Description,Status,Priority,Due Date,Assignee,Project ID")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString(task.getId().toString() + ",Test Report Task,A task for the report,IN_PROGRESS,HIGH")));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"COLLABORATOR"})
    void generateTaskReport_shouldReturnForbiddenForCollaborator() throws Exception {
        mockMvc.perform(get("/api/v1/reports/tasks")
                        .param("projectId", project.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
