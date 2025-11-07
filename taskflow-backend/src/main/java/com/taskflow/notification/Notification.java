package com.taskflow.notification;

import com.taskflow.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< HEAD

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
=======
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
<<<<<<< HEAD
public class Notification {
=======
@Entity
@Table(name = "notifications")
public class Notification {

>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "read_status", nullable = false)
    private Boolean readStatus = false;

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
