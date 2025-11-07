package com.taskflow.report;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import com.taskflow.task.Task;
import com.taskflow.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public String generateTaskReport(Long projectId) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);

        if (tasks.isEmpty()) {
            return "No tasks found for the given project ID.";
        }

        StringWriter sw = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(sw, ICSVWriter.DEFAULT_SEPARATOR, ICSVWriter.NO_QUOTE_CHARACTER, ICSVWriter.DEFAULT_ESCAPE_CHARACTER, ICSVWriter.DEFAULT_LINE_END)) {
            String[] header = {"ID", "Title", "Description", "Status", "Priority", "Due Date", "Assignee", "Project ID"};
            csvWriter.writeNext(header);

            for (Task task : tasks) {
                String assigneeName = task.getAssignee() != null ? task.getAssignee().getName() : "N/A";
                String[] data = {
                        task.getId().toString(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus().name(),
                        task.getPriority().name(),
                        task.getDueDate() != null ? task.getDueDate().toString() : "N/A",
                        assigneeName,
                        task.getProject().getId().toString()
                };
                csvWriter.writeNext(data);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error generating CSV report", e);
        }
        return sw.toString();
    }
}
