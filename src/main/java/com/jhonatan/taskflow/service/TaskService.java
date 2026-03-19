package com.jhonatan.taskflow.service;

import com.jhonatan.taskflow.dto.TaskRequest;
import com.jhonatan.taskflow.dto.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest request);

    TaskResponse getTaskById(Long taskId);

    List<TaskResponse> getTasksByProject(Long projectId);

    TaskResponse updateTaskStatus(Long taskId, String newStatus);

    void deleteTask(Long taskId);
}