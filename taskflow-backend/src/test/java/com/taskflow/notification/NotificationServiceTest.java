package com.taskflow.notification;

import com.taskflow.notification.dto.NotificationResponseDto;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
import com.taskflow.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationService notificationService;

    private User user;
    private User adminUser;
    private Notification notification;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).name("Test User").email("user@example.com").role(Role.COLLABORATOR).build();
        adminUser = User.builder().id(2L).name("Admin User").email("admin@example.com").role(Role.ADMIN).build();

        notification = Notification.builder()
                .id(1L)
                .user(user)
                .message("Test Message")
                .readStatus(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createNotification_shouldReturnCreatedNotification() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        NotificationResponseDto result = notificationService.createNotification(user.getId(), "Test Message");

        assertNotNull(result);
        assertEquals(notification.getMessage(), result.getMessage());
        assertFalse(result.getReadStatus());
    }

    @Test
    void createNotification_whenUserNotFound_shouldThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            notificationService.createNotification(99L, "Test Message");
        });
    }

    @Test
    void getNotificationsByUserId_whenAuthorizedUser_shouldReturnNotifications() {
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(anyLong()))
                .thenReturn(Arrays.asList(notification));

        List<NotificationResponseDto> result = notificationService.getNotificationsByUserId(user.getId(), user);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(notification.getMessage(), result.get(0).getMessage());
    }

    @Test
    void getNotificationsByUserId_whenAdminUser_shouldReturnNotifications() {
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(anyLong()))
                .thenReturn(Arrays.asList(notification));

        List<NotificationResponseDto> result = notificationService.getNotificationsByUserId(user.getId(), adminUser);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(notification.getMessage(), result.get(0).getMessage());
    }

    @Test
    void getNotificationsByUserId_whenUnauthorizedUser_shouldThrowAccessDeniedException() {
        User unauthorizedUser = User.builder().id(3L).role(Role.COLLABORATOR).build();

        assertThrows(AccessDeniedException.class, () -> {
            notificationService.getNotificationsByUserId(user.getId(), unauthorizedUser);
        });
    }

    @Test
    void markNotificationAsRead_whenAuthorizedUser_shouldMarkAsRead() {
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        NotificationResponseDto result = notificationService.markNotificationAsRead(notification.getId(), user);

        assertNotNull(result);
        assertTrue(result.getReadStatus());
    }

    @Test
    void markNotificationAsRead_whenAdminUser_shouldMarkAsRead() {
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        NotificationResponseDto result = notificationService.markNotificationAsRead(notification.getId(), adminUser);

        assertNotNull(result);
        assertTrue(result.getReadStatus());
    }

    @Test
    void markNotificationAsRead_whenNotificationNotFound_shouldThrowException() {
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            notificationService.markNotificationAsRead(99L, user);
        });
    }

    @Test
    void markNotificationAsRead_whenUnauthorizedUser_shouldThrowAccessDeniedException() {
        User unauthorizedUser = User.builder().id(3L).role(Role.COLLABORATOR).build();

        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification));

        assertThrows(AccessDeniedException.class, () -> {
            notificationService.markNotificationAsRead(notification.getId(), unauthorizedUser);
        });
    }
}
