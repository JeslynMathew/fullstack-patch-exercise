package com.internal.tasktracker;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/api/tasks")
    public ResponseEntity<?> searchTasks(
            @RequestParam(required = false, defaultValue = "") String q,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        // Normalize query input
        String query = q == null ? "" : q.trim();
        String searchTerm = "%" + query.toLowerCase() + "%";

        // Parse status filter
       String normalizedStatus = null;
if (status != null && !status.isBlank()) {
    try {
        normalizedStatus = TaskStatus.valueOf(status.trim().toUpperCase()).name();
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", "Invalid status: " + status));
    }
}

        // Query complexity estimation for logging
      

        System.out.println("[TaskController] q=\"" + query + "\" status=" + normalizedStatus
                + " page=" + page + " pageSize=" + pageSize
               );

        List<Task> allResults = taskRepository.searchTasks(searchTerm, normalizedStatus);

       int safePage = Math.max(page, 1);
int safePageSize = Math.min(Math.max(pageSize, 1), 100);

int start = (int) Math.min((long) (safePage - 1) * safePageSize, allResults.size());
int end = Math.min(start + safePageSize, allResults.size());
List<Task> pageResults = allResults.subList(start, end);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("items", pageResults);
        response.put("total", allResults.size());
        response.put("page", safePage);
        response.put("pageSize", safePageSize);

        return ResponseEntity.ok(response);
    }
}
