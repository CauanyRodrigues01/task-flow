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
<<<<<<< HEAD
    public TaskResponseDto createTask(Long projectId, TaskRequestDto taskRequestDto) {
        // ... (código existente)
=======
    public TaskResponseDto createTask(Long projectId, TaskRequestDto taskRequestDto, User currentUser) {
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
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

<<<<<<< HEAD
        if (assignee != null) {
            notificationService.createNotification(assignee.getId(), "Você foi atribuído à tarefa: " + savedTask.getTitle());
        }

        // Supondo que o usuário autenticado tem id 1L para fins de exemplo
        activityHistoryService.recordActivity(savedTask.getId(), 1L, "Tarefa '" + savedTask.getTitle() + "' foi criada.");
=======
        if (savedTask.getAssignee() != null) {
            notificationService.createNotification(savedTask.getAssignee().getId(), "Você foi atribuído à tarefa: " + savedTask.getTitle());
        }

        activityHistoryService.recordActivity(savedTask.getId(), currentUser.getId(), "Tarefa criada: " + savedTask.getTitle());
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)

        return mapToTaskResponseDto(savedTask);
    }

    @Transactional
    public TaskResponseDto updateTask(Long taskId, TaskRequestDto taskRequestDto, User currentUser) {
        Task oldTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

<<<<<<< HEAD
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
=======
        // Create a copy of the old task to compare against later
        Task task = oldTask; // Use oldTask as base for updates

        User oldAssignee = task.getAssignee();
        TaskStatus oldStatus = task.getStatus();
        String oldTitle = task.getTitle();
        String oldDescription = task.getDescription();
        TaskPriority oldPriority = task.getPriority();
        LocalDateTime oldDueDate = task.getDueDate();


        User newAssignee = null;
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
        if (taskRequestDto.getAssigneeId() != null) {
            newAssignee = userRepository.findById(taskRequestDto.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
        }

        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());
        task.setStatus(taskRequestDto.getStatus());
        task.setPriority(taskRequestDto.getPriority());
        task.setDueDate(taskRequestDto.getDueDate());
        task.setAssignee(newAssignee);

        Task updatedTask = taskRepository.save(task);

<<<<<<< HEAD
        if (changes.length() > 0) {
            // Supondo que o usuário autenticado tem id 1L para fins de exemplo
            activityHistoryService.recordActivity(updatedTask.getId(), 1L, changes.toString().trim());
=======
        // Record activity for changes
        if (!oldTitle.equals(updatedTask.getTitle())) {
            activityHistoryService.recordActivity(updatedTask.getId(), currentUser.getId(),
                    String.format("Título da tarefa alterado de '%s' para '%s'", oldTitle, updatedTask.getTitle()));
        }
        if (!oldDescription.equals(updatedTask.getDescription())) {
            activityHistoryService.recordActivity(updatedTask.getId(), currentUser.getId(),
                    String.format("Descrição da tarefa alterada de '%s' para '%s'", oldDescription, updatedTask.getDescription()));
        }
        if (!oldStatus.equals(updatedTask.getStatus())) {
            activityHistoryService.recordActivity(updatedTask.getId(), currentUser.getId(),
                    String.format("Status da tarefa alterado de '%s' para '%s'", oldStatus, updatedTask.getStatus()));
            // Notification for status change is already in updateTaskStatus, but this is for history
        }
        if (!oldPriority.equals(updatedTask.getPriority())) {
            activityHistoryService.recordActivity(updatedTask.getId(), currentUser.getId(),
                    String.format("Prioridade da tarefa alterada de '%s' para '%s'", oldPriority, updatedTask.getPriority()));
        }
        if (!java.util.Objects.equals(oldDueDate, updatedTask.getDueDate())) {
            activityHistoryService.recordActivity(updatedTask.getId(), currentUser.getId(),
                    String.format("Data de vencimento da tarefa alterada de '%s' para '%s'", oldDueDate, updatedTask.getDueDate()));
        }
        if (!java.util.Objects.equals(oldAssignee, updatedTask.getAssignee())) {
            String oldAssigneeName = (oldAssignee != null) ? oldAssignee.getName() : "Ninguém";
            String newAssigneeName = (updatedTask.getAssignee() != null) ? updatedTask.getAssignee().getName() : "Ninguém";
            activityHistoryService.recordActivity(updatedTask.getId(), currentUser.getId(),
                    String.format("Responsável da tarefa alterado de '%s' para '%s'", oldAssigneeName, newAssigneeName));
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
        }

        return mapToTaskResponseDto(updatedTask);
    }

    @Transactional
<<<<<<< HEAD
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        // Supondo que o usuário autenticado tem id 1L para fins de exemplo
        activityHistoryService.recordActivity(taskId, 1L, "Tarefa '" + task.getTitle() + "' foi excluída.");
=======
    public void deleteTask(Long taskId, User currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        activityHistoryService.recordActivity(taskId, currentUser.getId(), "Tarefa excluída: " + task.getTitle());

>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
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
    public TaskResponseDto updateTaskStatus(Long taskId, TaskStatusUpdateRequestDto statusUpdateRequestDto, User currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

<<<<<<< HEAD
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
=======
        TaskStatus oldStatus = task.getStatus(); // Get old status

        task.setStatus(statusUpdateRequestDto.getStatus());
        Task updatedTask = taskRepository.save(task);

        if (!oldStatus.equals(updatedTask.getStatus())) {
            // Notify Project Manager (project owner)
            User projectOwner = updatedTask.getProject().getOwner();
            notificationService.createNotification(projectOwner.getId(),
                    "O status da tarefa '" + updatedTask.getTitle() + "' foi alterado de " + oldStatus + " para " + updatedTask.getStatus());

            // Record activity for status change
            String activityDescription = "Status da tarefa '" + updatedTask.getTitle() + "' alterado de " + oldStatus + " para " + updatedTask.getStatus();
            if (statusUpdateRequestDto.getContextNote() != null && !statusUpdateRequestDto.getContextNote().isEmpty()) {
                activityDescription += ". Nota: " + statusUpdateRequestDto.getContextNote();
            }
            activityHistoryService.recordActivity(updatedTask.getId(), currentUser.getId(), activityDescription);
        }
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)

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
            dto.setAssigneeName(task.getAssignee().getName());
        }
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        return dto;
    }
}