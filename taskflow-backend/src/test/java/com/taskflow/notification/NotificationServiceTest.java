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
<<<<<<< HEAD
import org.springframework.security.access.AccessDeniedException;
=======
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
<<<<<<< HEAD
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
=======
import static org.mockito.Mockito.*;
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
<<<<<<< HEAD

=======
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationService notificationService;

    private User user;
<<<<<<< HEAD
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
=======
    private Notification notification1;
    private Notification notification2;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .role(Role.COLLABORATOR)
                .build();

        notification1 = Notification.builder()
                .id(10L)
                .user(user)
                .message("Notification 1")
                .readStatus(false)
                .createdAt(LocalDateTime.now().minusHours(1))
                .build();

        notification2 = Notification.builder()
                .id(11L)
                .user(user)
                .message("Notification 2")
                .readStatus(true)
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
<<<<<<< HEAD
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
=======
    void createNotification_shouldCreateAndReturnNotification() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification1);

        NotificationResponseDto result = notificationService.createNotification(user.getId(), "New message");

        assertNotNull(result);
        assertEquals(notification1.getId(), result.getId());
        assertEquals("New message", result.getMessage());
        assertFalse(result.getReadStatus());
        verify(userRepository, times(1)).findById(user.getId());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void createNotification_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationService.createNotification(user.getId(), "New message");
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(user.getId());
        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    void getNotificationsByUserId_shouldReturnNotifications() {
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId()))
                .thenReturn(Arrays.asList(notification2, notification1));

        List<NotificationResponseDto> results = notificationService.getNotificationsByUserId(user.getId());

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(notification2.getId(), results.get(0).getId());
        assertEquals(notification1.getId(), results.get(1).getId());
        verify(notificationRepository, times(1)).findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    @Test
    void markNotificationAsRead_shouldMarkNotificationAsRead() {
        when(notificationRepository.findById(notification1.getId())).thenReturn(Optional.of(notification1));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification notif = invocation.getArgument(0);
            notif.setReadStatus(true);
            return notif;
        });

        NotificationResponseDto result = notificationService.markNotificationAsRead(notification1.getId());

        assertNotNull(result);
        assertTrue(result.getReadStatus());
        verify(notificationRepository, times(1)).findById(notification1.getId());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void markNotificationAsRead_shouldThrowException_whenNotificationNotFound() {
        when(notificationRepository.findById(notification1.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationService.markNotificationAsRead(notification1.getId());
        });

        assertEquals("Notification not found", exception.getMessage());
        verify(notificationRepository, times(1)).findById(notification1.getId());
        verify(notificationRepository, never()).save(any(Notification.class));
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
    }
}
