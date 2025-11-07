package com.taskflow.activityhistory;

import com.taskflow.task.Task;
import com.taskflow.user.User;
<<<<<<< HEAD
=======
import jakarta.persistence.*;
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< HEAD

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_history")
=======
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
<<<<<<< HEAD
=======
@Entity
@Table(name = "activity_history")
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
public class ActivityHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
<<<<<<< HEAD
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
=======
    @JoinColumn(name = "user_id") // user_id can be null if system action
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
}
