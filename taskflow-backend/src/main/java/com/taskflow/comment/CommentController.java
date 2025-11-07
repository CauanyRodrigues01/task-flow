package com.taskflow.comment;

import com.taskflow.comment.dto.CommentRequestDto;
import com.taskflow.comment.dto.CommentResponseDto;
import com.taskflow.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
<<<<<<< HEAD
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks/{taskId}/comments")
=======
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks/{taskId}/comments")
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
<<<<<<< HEAD
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CommentRequestDto commentDto,
            @AuthenticationPrincipal User author
    ) {
        CommentResponseDto createdComment = commentService.createComment(taskId, commentDto, author);
=======
    @PreAuthorize("isAuthenticated()") // Further checks are done in service layer for project membership
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CommentRequestDto commentRequestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        CommentResponseDto createdComment = commentService.createComment(taskId, commentRequestDto, currentUser);
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }
}
