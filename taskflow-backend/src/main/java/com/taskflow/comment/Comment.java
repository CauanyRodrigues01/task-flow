package com.taskflow.comment;

import com.taskflow.task.Task;
import com.taskflow.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< HEAD

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
=======
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
<<<<<<< HEAD
public class Comment {
=======
@Entity
@Table(name = "comments")
public class Comment {

>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

<<<<<<< HEAD
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
=======
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
}
