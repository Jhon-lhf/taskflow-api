package com.jhonatan.taskflow.dto;

import com.jhonatan.taskflow.domain.enums.TaskPriority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TaskRequest {

    @NotBlank(message = "Task title must not be empty")
    private String title;

    private String description;

    @NotNull(message = "Priority is required")
    private TaskPriority priority;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    private String assignedTo; // username — opcional

    @FutureOrPresent(message = "Due date must be today or in the future")
    private LocalDate dueDate;
}