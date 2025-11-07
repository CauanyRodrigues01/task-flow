package com.taskflow.notification;

import com.taskflow.notification.dto.NotificationResponseDto;
import com.taskflow.user.User;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
=======
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/users/{userId}/notifications")
<<<<<<< HEAD
    @PreAuthorize("#userId == authentication.principal.id or hasAuthority('ADMIN')")
    public ResponseEntity<List<NotificationResponseDto>> getNotificationsByUserId(
            @PathVariable Long userId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        List<NotificationResponseDto> notifications = notificationService.getNotificationsByUserId(userId, authenticatedUser);
=======
    @PreAuthorize("isAuthenticated() and (#userId == authentication.principal.id or hasRole('ADMIN'))")
    public ResponseEntity<List<NotificationResponseDto>> getNotificationsByUserId(@PathVariable Long userId) {
        List<NotificationResponseDto> notifications = notificationService.getNotificationsByUserId(userId);
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/notifications/{notificationId}/read")
<<<<<<< HEAD
    @PreAuthorize("@notificationRepository.findById(#notificationId).orElse(null)?.user?.id == authentication.principal.id or hasAuthority('ADMIN')")
    public ResponseEntity<NotificationResponseDto> markNotificationAsRead(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        NotificationResponseDto updatedNotification = notificationService.markNotificationAsRead(notificationId, authenticatedUser);
=======
    @PreAuthorize("isAuthenticated() and (@notificationService.getNotificationById(#notificationId).userId == authentication.principal.id or hasRole('ADMIN'))")
    public ResponseEntity<NotificationResponseDto> markNotificationAsRead(@PathVariable Long notificationId) {
        NotificationResponseDto updatedNotification = notificationService.markNotificationAsRead(notificationId);
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
        return ResponseEntity.ok(updatedNotification);
    }
}
