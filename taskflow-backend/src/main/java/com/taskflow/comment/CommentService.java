package com.taskflow.comment;

import com.taskflow.activityhistory.ActivityHistoryService;
import com.taskflow.comment.dto.CommentRequestDto;
import com.taskflow.comment.dto.CommentResponseDto;
import com.taskflow.notification.NotificationService;
import com.taskflow.task.Task;
import com.taskflow.task.TaskRepository;
import com.taskflow.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final NotificationService notificationService;
    private final ActivityHistoryService activityHistoryService;

    @Transactional
    public CommentResponseDto createComment(Long taskId, CommentRequestDto commentDto, User author) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        boolean isMember = task.getProject().getMembers().stream()
                .anyMatch(member -> member.getId().equals(author.getId()));

        if (!isMember) {
            throw new AccessDeniedException("User is not a member of the project and cannot comment on the task");
        }

        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .task(task)
                .author(author)
                .build();

        Comment savedComment = commentRepository.save(comment);

        task.getProject().getMembers().stream()
                .filter(member -> !member.getId().equals(author.getId()))
                .forEach(member -> notificationService.createNotification(member.getId(), "Novo comentário na tarefa '" + task.getTitle() + "': " + savedComment.getContent()));

        String activityDescription = "adicionou um comentário: \"" + savedComment.getContent() + "\"";
        activityHistoryService.recordActivity(taskId, author.getId(), activityDescription);

        return toDto(savedComment);
    }

    private CommentResponseDto toDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .taskId(comment.getTask().getId())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getName())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
