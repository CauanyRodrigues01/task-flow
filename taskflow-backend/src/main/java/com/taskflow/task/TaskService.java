package com.taskflow.task;

import com.taskflow.project.Project;
import com.taskflow.project.ProjectRepository;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
import com.taskflow.task.dto.TaskRequestDto;
import com.taskflow.task.dto.TaskResponseDto;
import com.taskflow.task.dto.TaskStatusUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    // private final ActivityHistoryService activityHistoryService; // Será injetado posteriormente

    @Transactional
    public TaskResponseDto createTask(Long projectId, TaskRequestDto taskRequestDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User assignee = null;
        if (taskRequestDto.getAssigneeId() != null) {
            assignee = userRepository.findById(taskRequestDto.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            // Validar se o assignee é membro do projeto (a ser implementado)
        }

        Task task = Task.builder()
                .title(taskRequestDto.getTitle())
                .description(taskRequestDto.getDescription())
                .status(taskRequestDto.getStatus() != null ? taskRequestDto.getStatus() : TaskStatus.TODO)
                .priority(taskRequestDto.getPriority() != null ? taskRequestDto.getPriority() : TaskPriority.MEDIUM)
                .dueDate(taskRequestDto.getDueDate())
                .project(project)
                .assignee(assignee)
                .build();

        Task savedTask = taskRepository.save(task);

        // activityHistoryService.recordActivity(savedTask.getId(), null, "Tarefa criada"); // Será implementado posteriormente

        return mapToTaskResponseDto(savedTask);
    }

    @Transactional
    public TaskResponseDto updateTask(Long taskId, TaskRequestDto taskRequestDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User assignee = null;
        if (taskRequestDto.getAssigneeId() != null) {
            assignee = userRepository.findById(taskRequestDto.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            // Validar se o assignee é membro do projeto (a ser implementado)
        }

        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());
        task.setStatus(taskRequestDto.getStatus());
        task.setPriority(taskRequestDto.getPriority());
        task.setDueDate(taskRequestDto.getDueDate());
        task.setAssignee(assignee);

        Task updatedTask = taskRepository.save(task);

        // activityHistoryService.recordActivity(updatedTask.getId(), null, "Tarefa atualizada"); // Será implementado posteriormente

        return mapToTaskResponseDto(updatedTask);
    }

    @Transactional
    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(taskId);
        // activityHistoryService.recordActivity(taskId, null, "Tarefa excluída"); // Será implementado posteriormente
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByProjectId(Long projectId, TaskStatus status, Long assigneeId, TaskPriority priority, String keyword) {
        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException("Project not found");
        }

        Specification<Task> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("project").get("id"), projectId));

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (assigneeId != null) {
                predicates.add(criteriaBuilder.equal(root.get("assignee").get("id"), assigneeId));
            }
            if (priority != null) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), priority));
            }
            if (keyword != null && !keyword.isEmpty()) {
                Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
                Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + keyword.toLowerCase() + "%");
                predicates.add(criteriaBuilder.or(titlePredicate, descriptionPredicate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return taskRepository.findAll(spec).stream()
                .map(this::mapToTaskResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TaskResponseDto updateTaskStatus(Long taskId, TaskStatusUpdateRequestDto statusUpdateRequestDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(statusUpdateRequestDto.getStatus());
        Task updatedTask = taskRepository.save(task);

        // activityHistoryService.recordActivity(updatedTask.getId(), null, "Status da tarefa atualizado"); // Será implementado posteriormente

        return mapToTaskResponseDto(updatedTask);
    }

    private TaskResponseDto mapToTaskResponseDto(Task task) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setDueDate(task.getDueDate());
        dto.setProjectId(task.getProject().getId());
        if (task.getAssignee() != null) {
            dto.setAssigneeId(task.getAssignee().getId());
        }
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        return dto;
    }
}
