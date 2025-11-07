package com.taskflow.activityhistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityHistoryRepository extends JpaRepository<ActivityHistory, Long> {
<<<<<<< HEAD
    List<ActivityHistory> findByTaskIdOrderByCreatedAtDesc(Long taskId);
=======
    List<ActivityHistory> findByTaskIdOrderByCreatedAtAsc(Long taskId);
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
}
