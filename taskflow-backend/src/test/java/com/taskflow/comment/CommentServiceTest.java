package com.taskflow.comment;

import com.taskflow.activityhistory.ActivityHistoryService;
import com.taskflow.comment.dto.CommentRequestDto;
import com.taskflow.comment.dto.CommentResponseDto;
import com.taskflow.project.Project;
import com.taskflow.task.Task;
import com.taskflow.task.TaskRepository;
import com.taskflow.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ActivityHistoryService activityHistoryService;

    @InjectMocks
    private CommentService commentService;

    private User author;
    private Project project;
    private Task task;
    private Comment comment;
    private CommentRequestDto commentRequestDto;

    @BeforeEach
    void setUp() {
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
