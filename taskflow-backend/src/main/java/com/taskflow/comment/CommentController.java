package com.taskflow.comment;

import com.taskflow.comment.dto.CommentRequestDto;
import com.taskflow.comment.dto.CommentResponseDto;
import com.taskflow.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CommentRequestDto commentDto,
            @AuthenticationPrincipal User author
    ) {
        CommentResponseDto createdComment = commentService.createComment(taskId, commentDto, author);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }
}
