package com.taskflow.notification;

import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD

import java.util.List;

=======
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
}
