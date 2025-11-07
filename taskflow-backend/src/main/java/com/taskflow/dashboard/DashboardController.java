package com.taskflow.dashboard;

import com.taskflow.dashboard.dto.DashboardResponseDto;
import com.taskflow.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROJECT_MANAGER')")
    public ResponseEntity<DashboardResponseDto> getDashboardData(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) Long projectId) {
        DashboardResponseDto dashboardData = dashboardService.getDashboardData(currentUser.getId(), projectId);
        return ResponseEntity.ok(dashboardData);
    }
}
