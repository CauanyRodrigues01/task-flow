package com.taskflow.activityhistory;

import com.taskflow.activityhistory.dto.ActivityHistoryResponse;
import com.taskflow.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ActivityHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityHistoryService activityHistoryService;

    @Test
    @WithMockUser(username = "test@example.com", roles = {"ADMIN"})
    void getTaskHistory_shouldReturnOk() throws Exception {
        // Given
        Long taskId = 1L;
        List<ActivityHistoryResponse> mockHistory = Arrays.asList(
                new ActivityHistoryResponse(1L, taskId, 1L, "User created task", LocalDateTime.now()),
                new ActivityHistoryResponse(2L, taskId, 1L, "Task status changed to IN_PROGRESS", LocalDateTime.now())
        );
        when(activityHistoryService.getTaskHistory(anyLong())).thenReturn(mockHistory);

        // When & Then
        mockMvc.perform(get("/tasks/{taskId}/history", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(mockHistory.size()))
                .andExpect(jsonPath("$[0].description").value("User created task"));
    }
}
