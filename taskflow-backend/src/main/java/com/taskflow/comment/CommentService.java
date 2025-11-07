package com.taskflow.comment;

<<<<<<< HEAD
import com.taskflow.activityhistory.ActivityHistoryService;
import com.taskflow.comment.dto.CommentRequestDto;
import com.taskflow.comment.dto.CommentResponseDto;
import com.taskflow.notification.NotificationService;
=======
import com.taskflow.comment.dto.CommentRequestDto;
import com.taskflow.comment.dto.CommentResponseDto;
import com.taskflow.notification.NotificationService;
import com.taskflow.activityhistory.ActivityHistoryService;
import com.taskflow.project.Project;
import com.taskflow.project.ProjectRepository;
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
import com.taskflow.task.Task;
import com.taskflow.task.TaskRepository;
import com.taskflow.user.User;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

=======
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
<<<<<<< HEAD
=======
    private final ProjectRepository projectRepository;
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
    private final NotificationService notificationService;
    private final ActivityHistoryService activityHistoryService;

    @Transactional
    public CommentResponseDto createComment(Long taskId, CommentRequestDto commentDto, User author) {
        Task task = taskRepository.findById(taskId)
<<<<<<< HEAD
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        boolean isMember = task.getProject().getMembers().stream()
                .anyMatch(member -> member.getId().equals(author.getId()));

        if (!isMember) {
            throw new AccessDeniedException("User is not a member of the project and cannot comment on the task");
=======
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Check if the author is a member of the project associated with the task
        Project project = task.getProject();
        if (!project.getMembers().contains(author) && !project.getOwner().equals(author)) {
            throw new RuntimeException("User is not a member of the project and cannot comment on this task.");
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
        }

        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .task(task)
                .author(author)
                .build();

        Comment savedComment = commentRepository.save(comment);

<<<<<<< HEAD
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
=======
        // Send notifications to project members (excluding the author) and project owner
        String notificationMessage = String.format("Novo comentário na tarefa '%s' do projeto '%s' por %s: %s",
                task.getTitle(), project.getName(), author.getName(), commentDto.getContent());

        Set<User> recipients = new HashSet<>(project.getMembers());
        recipients.add(project.getOwner());
        if (task.getAssignee() != null) {
            recipients.add(task.getAssignee());
        }

        recipients.remove(author); // Don't notify the author themselves

        for (User recipient : recipients) {
            notificationService.createNotification(recipient.getId(), notificationMessage);
        }

        // Record activity for new comment
        activityHistoryService.recordActivity(savedComment.getTask().getId(), author.getId(),
                "Novo comentário adicionado à tarefa '" + savedComment.getTask().getTitle() + "' por " + author.getName() + ": " + savedComment.getContent());


        return mapToCommentResponseDto(savedComment);
    }

    private CommentResponseDto mapToCommentResponseDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setTaskId(comment.getTask().getId());
        dto.setAuthorId(comment.getAuthor().getId());
        dto.setAuthorName(comment.getAuthor().getName());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
    }
}
