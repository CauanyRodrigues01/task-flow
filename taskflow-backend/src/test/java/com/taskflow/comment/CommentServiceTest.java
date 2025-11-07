package com.taskflow.comment;

<<<<<<< HEAD
import com.taskflow.activityhistory.ActivityHistoryService;
import com.taskflow.comment.dto.CommentRequestDto;
import com.taskflow.comment.dto.CommentResponseDto;
import com.taskflow.project.Project;
import com.taskflow.task.Task;
import com.taskflow.task.TaskRepository;
import com.taskflow.user.User;
=======
import com.taskflow.comment.dto.CommentRequestDto;
import com.taskflow.comment.dto.CommentResponseDto;
import com.taskflow.notification.NotificationService;
import com.taskflow.activityhistory.ActivityHistoryService;
import com.taskflow.project.Project;
import com.taskflow.project.ProjectRepository;
import com.taskflow.task.Task;
import com.taskflow.task.TaskRepository;
import com.taskflow.user.User;
import com.taskflow.user.Role;
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
<<<<<<< HEAD
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
=======
import static org.mockito.Mockito.*;
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
<<<<<<< HEAD

    @Mock
    private TaskRepository taskRepository;

=======
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private NotificationService notificationService;
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
    @Mock
    private ActivityHistoryService activityHistoryService;

    @InjectMocks
    private CommentService commentService;

    private User author;
<<<<<<< HEAD
    private Project project;
    private Task task;
    private Comment comment;
=======
    private User nonMember;
    private Project project;
    private Task task;
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
    private CommentRequestDto commentRequestDto;

    @BeforeEach
    void setUp() {
<<<<<<< HEAD
        author = User.builder().id(1L).name("John Doe").build();
        
        project = Project.builder().id(1L).name("Test Project").build();
        Set<User> members = new HashSet<>();
        members.add(author);
        project.setMembers(members);

        task = Task.builder().id(1L).title("Test Task").project(project).build();
        
        commentRequestDto = new CommentRequestDto();
        commentRequestDto.setContent("Test comment");

        comment = Comment.builder()
                .id(1L)
                .content("Test comment")
                .task(task)
                .author(author)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createComment_whenUserIsMember_shouldCreateCommentAndRecordActivity() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponseDto result = commentService.createComment(1L, commentRequestDto, author);

        assertNotNull(result);
        assertEquals("Test comment", result.getContent());
        assertEquals(1L, result.getTaskId());
        assertEquals(1L, result.getAuthorId());
        verify(activityHistoryService).recordActivity(anyLong(), anyLong(), anyString());
    }

    @Test
    void createComment_whenTaskNotFound_shouldThrowException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            commentService.createComment(1L, commentRequestDto, author);
        });
    }

    @Test
    void createComment_whenUserIsNotMember_shouldThrowAccessDeniedException() {
        User nonMember = User.builder().id(2L).build();
        project.setMembers(new HashSet<>()); // Ensure author is not a member

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(AccessDeniedException.class, () -> {
            commentService.createComment(1L, commentRequestDto, nonMember);
        });
    }
}
=======
        author = User.builder()
                .id(1L)
                .name("Author User")
                .email("author@example.com")
                .role(Role.COLLABORATOR)
                .build();

        nonMember = User.builder()
                .id(2L)
                .name("Non Member")
                .email("nonmember@example.com")
                .role(Role.COLLABORATOR)
                .build();

        project = Project.builder()
                .id(10L)
                .name("Test Project")
                .owner(author) // Author is the owner
                .members(new HashSet<>(Set.of(author))) // Author is also a member
                .build();

        task = Task.builder()
                .id(100L)
                .title("Test Task")
                .project(project)
                .build();

        commentRequestDto = new CommentRequestDto();
        commentRequestDto.setContent("This is a test comment.");
    }

    @Test
    void createComment_shouldCreateCommentSuccessfully_whenUserIsProjectMember() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment comment = invocation.getArgument(0);
            comment.setId(1L);
            comment.setCreatedAt(LocalDateTime.now());
            return comment;
        });

        CommentResponseDto result = commentService.createComment(task.getId(), commentRequestDto, author);

        assertNotNull(result);
        assertEquals(commentRequestDto.getContent(), result.getContent());
        assertEquals(task.getId(), result.getTaskId());
        assertEquals(author.getId(), result.getAuthorId());
        assertEquals(author.getName(), result.getAuthorName());
        verify(commentRepository, times(1)).save(any(Comment.class));
        verify(notificationService, times(anyInt())).createNotification(anyLong(), anyString()); // Verify notification
        verify(activityHistoryService, times(1)).recordActivity(eq(task.getId()), eq(author.getId()), anyString()); // Verify activity record
    }

    @Test
    void createComment_shouldThrowException_whenTaskNotFound() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commentService.createComment(task.getId(), commentRequestDto, author);
        });

        assertEquals("Task not found", exception.getMessage());
        verify(commentRepository, never()).save(any(Comment.class));
        verify(notificationService, never()).createNotification(anyLong(), anyString()); // No notification
        verify(activityHistoryService, never()).recordActivity(anyLong(), anyLong(), anyString()); // No activity record
    }

    @Test
    void createComment_shouldThrowException_whenUserIsNotProjectMember() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commentService.createComment(task.getId(), commentRequestDto, nonMember);
        });

        assertEquals("User is not a member of the project and cannot comment on this task.", exception.getMessage());
        verify(commentRepository, never()).save(any(Comment.class));
        verify(notificationService, never()).createNotification(anyLong(), anyString()); // No notification
        verify(activityHistoryService, never()).recordActivity(anyLong(), anyLong(), anyString()); // No activity record
    }
}
>>>>>>> 543abc6 (wip: salva alterações locais antes do rebase)
