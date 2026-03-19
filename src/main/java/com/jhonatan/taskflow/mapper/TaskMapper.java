package com.jhonatan.taskflow.mapper;

import com.jhonatan.taskflow.domain.entity.Task;
import com.jhonatan.taskflow.dto.TaskResponse;

public class TaskMapper {
    public static TaskResponse toTaskResponse(Task task) {
        if (task == null) return null;

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .projectId(task.getProject().getId())
                // Si la tarea no está asignada a nadie, assignedTo será null — correcto
                .assignedTo(task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : null)
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .build();
    }
}
