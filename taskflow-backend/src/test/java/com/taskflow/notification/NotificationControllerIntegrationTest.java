package com.taskflow.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.notification.dto.NotificationResponseDto;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.taskflow.TestDatabaseConfig;
import org.springframework.context.annotation.Import;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class NotificationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private User user;
    private User adminUser;
    private Notification notification1;
    private Notification notification2;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder().email("user@example.com").passwordHash("password").name("Test User").role(Role.COLLABORATOR).build());
        adminUser = userRepository.save(User.builder().email("admin@example.com").passwordHash("password").name("Admin User").role(Role.ADMIN).build());

        notification1 = notificationRepository.save(Notification.builder()
                .user(user)
                .message("Notification 1 for user")
                .readStatus(false)
                .createdAt(LocalDateTime.now().minusHours(1))
                .build());

        notification2 = notificationRepository.save(Notification.builder()
                .user(user)
                .message("Notification 2 for user")
                .readStatus(true)
                .createdAt(LocalDateTime.now())
                .build());

        // Notification for admin, to ensure separation
        notificationRepository.save(Notification.builder()
                .user(adminUser)
                .message("Notification for admin")
                .readStatus(false)
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void getNotificationsByUserId_whenAuthorizedUser_shouldReturnNotifications() throws Exception {
        mockMvc.perform(get("/api/v1/users/{userId}/notifications", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].message").value(notification2.getMessage())) // Newest first
                .andExpect(jsonPath("$[1].message").value(notification1.getMessage()));
    }

    @Test
    @WithMockUser(username = "admin@example.com")
    void getNotificationsByUserId_whenAdminUser_shouldReturnAllNotificationsForTargetUser() throws Exception {
        mockMvc.perform(get("/api/v1/users/{userId}/notifications", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].message").value(notification2.getMessage()))
                .andExpect(jsonPath("$[1].message").value(notification1.getMessage()));
    }

    @Test
    @WithMockUser(username = "other@example.com")
    void getNotificationsByUserId_whenUnauthorizedUser_shouldReturnForbidden() throws Exception {
        User otherUser = userRepository.save(User.builder().email("other@example.com").passwordHash("password").name("Other User").role(Role.COLLABORATOR).build());

        mockMvc.perform(get("/api/v1/users/{userId}/notifications", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void markNotificationAsRead_whenAuthorizedUser_shouldReturnUpdatedNotification() throws Exception {
        mockMvc.perform(patch("/api/v1/notifications/{notificationId}/read", notification1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notification1.getId()))
                .andExpect(jsonPath("$.readStatus").value(true));
    }

    @Test
    @WithMockUser(username = "admin@example.com")
    void markNotificationAsRead_whenAdminUser_shouldReturnUpdatedNotification() throws Exception {
        mockMvc.perform(patch("/api/v1/notifications/{notificationId}/read", notification1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notification1.getId()))
                .andExpect(jsonPath("$.readStatus").value(true));
    }

    @Test
    @WithMockUser(username = "other@example.com")
    void markNotificationAsRead_whenUnauthorizedUser_shouldReturnForbidden() throws Exception {
        User otherUser = userRepository.save(User.builder().email("other@example.com").passwordHash("password").name("Other User").role(Role.COLLABORATOR).build());

        mockMvc.perform(patch("/api/v1/notifications/{notificationId}/read", notification1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void markNotificationAsRead_whenNotAuthenticated_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(patch("/api/v1/notifications/{notificationId}/read", notification1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
