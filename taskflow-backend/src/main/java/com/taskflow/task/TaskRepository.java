package com.taskflow.task;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    Long countByProjectIdIn(List<Long> projectIds);

    @Query("SELECT t.status, COUNT(t) FROM Task t WHERE t.project.id IN :projectIds GROUP BY t.status")
    List<Object[]> countTasksByStatusForProjects(@Param("projectIds") List<Long> projectIds);

    List<Task> findByProjectId(Long projectId);

    List<Task> findByAssigneeId(Long assigneeId, Sort sort);
}