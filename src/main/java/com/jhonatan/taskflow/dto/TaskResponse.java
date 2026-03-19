package com.jhonatan.taskflow.dto;

import com.jhonatan.taskflow.domain.enums.TaskPriority;
import com.jhonatan.taskflow.domain.enums.TaskStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long projectId;
    private String assignedTo;   // username del asignado, puede ser null
    private LocalDate dueDate;
    private LocalDateTime createdAt;
}