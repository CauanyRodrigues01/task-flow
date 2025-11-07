package com.taskflow.notification;

import com.taskflow.notification.dto.NotificationResponseDto;
import com.taskflow.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/users/{userId}/notifications")
    @PreAuthorize("#userId == authentication.principal.id or hasAuthority('ADMIN')")
    public ResponseEntity<List<NotificationResponseDto>> getNotificationsByUserId(
            @PathVariable Long userId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        List<NotificationResponseDto> notifications = notificationService.getNotificationsByUserId(userId, authenticatedUser);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/notifications/{notificationId}/read")
    @PreAuthorize("@notificationRepository.findById(#notificationId).orElse(null)?.user?.id == authentication.principal.id or hasAuthority('ADMIN')")
    public ResponseEntity<NotificationResponseDto> markNotificationAsRead(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        NotificationResponseDto updatedNotification = notificationService.markNotificationAsRead(notificationId, authenticatedUser);
        return ResponseEntity.ok(updatedNotification);
    }
}
