package com.taskflow.task;

import com.taskflow.activityhistory.ActivityHistoryService;
import com.taskflow.notification.NotificationService;
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
import org.springframework.data.domain.Sort;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final ActivityHistoryService activityHistoryService;

    @Transactional
    public TaskResponseDto createTask(Long projectId, TaskRequestDto taskRequestDto) {
        // ... (código existente)
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User assignee = null;
        if (taskRequestDto.getAssigneeId() != null) {
            assignee = userRepository.findById(taskRequestDto.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
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

        if (assignee != null) {
            notificationService.createNotification(assignee.getId(), "Você foi atribuído à tarefa: " + savedTask.getTitle());
        }

        // Supondo que o usuário autenticado tem id 1L para fins de exemplo
        activityHistoryService.recordActivity(savedTask.getId(), 1L, "Tarefa '" + savedTask.getTitle() + "' foi criada.");

        return mapToTaskResponseDto(savedTask);
    }

    @Transactional
    public TaskResponseDto updateTask(Long taskId, TaskRequestDto taskRequestDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        StringBuilder changes = new StringBuilder();
        // Lógica para registrar alterações detalhadas
        if (!Objects.equals(task.getTitle(), taskRequestDto.getTitle())) {
            changes.append("Título alterado de '").append(task.getTitle()).append("' para '").append(taskRequestDto.getTitle()).append("'. ");
        }
        if (!Objects.equals(task.getDescription(), taskRequestDto.getDescription())) {
            changes.append("Descrição alterada. ");
        }
        if (!Objects.equals(task.getStatus(), taskRequestDto.getStatus())) {
            changes.append("Status alterado de '").append(task.getStatus()).append("' para '").append(taskRequestDto.getStatus()).append("'. ");
        }
        if (!Objects.equals(task.getPriority(), taskRequestDto.getPriority())) {
            changes.append("Prioridade alterada de '").append(task.getPriority()).append("' para '").append(taskRequestDto.getPriority()).append("'. ");
        }
        if (!Objects.equals(task.getDueDate(), taskRequestDto.getDueDate())) {
            changes.append("Data de vencimento alterada. ");
        }
        if (!Objects.equals(task.getAssignee() != null ? task.getAssignee().getId() : null, taskRequestDto.getAssigneeId())) {
             User oldAssignee = task.getAssignee();
             User newAssignee = taskRequestDto.getAssigneeId() != null ? userRepository.findById(taskRequestDto.getAssigneeId()).orElse(null) : null;
             changes.append("Responsável alterado de '")
                    .append(oldAssignee != null ? oldAssignee.getName() : "Ninguém")
                    .append("' para '")
                    .append(newAssignee != null ? newAssignee.getName() : "Ninguém")
                    .append("'. ");
        }


        User assignee = null;
        if (taskRequestDto.getAssigneeId() != null) {
            assignee = userRepository.findById(taskRequestDto.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
        }

        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());
        task.setStatus(taskRequestDto.getStatus());
        task.setPriority(taskRequestDto.getPriority());
        task.setDueDate(taskRequestDto.getDueDate());
        task.setAssignee(assignee);

        Task updatedTask = taskRepository.save(task);

        if (changes.length() > 0) {
            // Supondo que o usuário autenticado tem id 1L para fins de exemplo
            activityHistoryService.recordActivity(updatedTask.getId(), 1L, changes.toString().trim());
        }

        return mapToTaskResponseDto(updatedTask);
    }

    @Transactional
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        // Supondo que o usuário autenticado tem id 1L para fins de exemplo
        activityHistoryService.recordActivity(taskId, 1L, "Tarefa '" + task.getTitle() + "' foi excluída.");
        taskRepository.deleteById(taskId);
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByProjectId(Long projectId, TaskStatus status, Long assigneeId, TaskPriority priority, String keyword) {
        // ... (código existente)
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

        TaskStatus oldStatus = task.getStatus();
        task.setStatus(statusUpdateRequestDto.getStatus());
        Task updatedTask = taskRepository.save(task);

        notificationService.createNotification(updatedTask.getProject().getOwner().getId(), "O status da tarefa '" + updatedTask.getTitle() + "' foi alterado para: " + updatedTask.getStatus());

        StringBuilder descriptionBuilder = new StringBuilder();
        descriptionBuilder.append("Status da tarefa '").append(updatedTask.getTitle()).append("' atualizado de '").append(oldStatus).append("' para '").append(updatedTask.getStatus()).append("'.");

        if (statusUpdateRequestDto.getContextNote() != null && !statusUpdateRequestDto.getContextNote().isBlank()) {
            descriptionBuilder.append(" Nota: ").append(statusUpdateRequestDto.getContextNote());
        }
        
        // Supondo que o usuário autenticado tem id 1L para fins de exemplo
        activityHistoryService.recordActivity(updatedTask.getId(), 1L, descriptionBuilder.toString());

        return mapToTaskResponseDto(updatedTask);
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByAssigneeId(Long assigneeId, String sortBy, String order) {
        Sort sort = Sort.by(Sort.Direction.ASC, "dueDate", "priority"); // Default sort
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
            sort = Sort.by(direction, sortBy);
        }
        return taskRepository.findByAssigneeId(assigneeId, sort).stream()
                .map(this::mapToTaskResponseDto)
                .collect(Collectors.toList());
    }

    private TaskResponseDto mapToTaskResponseDto(Task task) {
        // ... (código existente)
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