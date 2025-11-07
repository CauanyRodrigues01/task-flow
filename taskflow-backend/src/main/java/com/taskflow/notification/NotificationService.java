package com.taskflow.notification;

import com.taskflow.notification.dto.NotificationResponseDto;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
import com.taskflow.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public NotificationResponseDto createNotification(Long userId, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Notification notification = Notification.builder()
                .user(user)
                .message(message)
                .readStatus(false)
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        return toDto(savedNotification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getNotificationsByUserId(Long userId, User authenticatedUser) {
        if (!userId.equals(authenticatedUser.getId()) && !authenticatedUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("User is not authorized to view these notifications");
        }
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return notifications.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public NotificationResponseDto markNotificationAsRead(Long notificationId, User authenticatedUser) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

        if (!notification.getUser().getId().equals(authenticatedUser.getId()) && !authenticatedUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("User is not authorized to mark this notification as read");
        }

        notification.setReadStatus(true);
        Notification updatedNotification = notificationRepository.save(notification);
        return toDto(updatedNotification);
    }

    private NotificationResponseDto toDto(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .userId(notification.getUser().getId())
                .message(notification.getMessage())
                .readStatus(notification.getReadStatus())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}