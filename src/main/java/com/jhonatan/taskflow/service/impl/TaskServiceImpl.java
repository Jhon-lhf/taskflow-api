package com.jhonatan.taskflow.service.impl;

import com.jhonatan.taskflow.domain.entity.Task;
import com.jhonatan.taskflow.domain.entity.User;
import com.jhonatan.taskflow.domain.enums.TaskStatus;
import com.jhonatan.taskflow.dto.TaskRequest;
import com.jhonatan.taskflow.dto.TaskResponse;
import com.jhonatan.taskflow.exception.BusinessException;
import com.jhonatan.taskflow.exception.ProjectNotFoundException;
import com.jhonatan.taskflow.exception.TaskNotFoundException;
import com.jhonatan.taskflow.exception.UserNotFoundException;
import com.jhonatan.taskflow.mapper.TaskMapper;
import com.jhonatan.taskflow.repository.ProjectRepository;
import com.jhonatan.taskflow.repository.TaskRepository;
import com.jhonatan.taskflow.repository.UserRepository;
import com.jhonatan.taskflow.service.TaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public TaskResponse createTask(TaskRequest request) {
        var project = projectRepository.findById(request.getProjectId()).orElseThrow(
                () -> new ProjectNotFoundException("Project not found with ID " + request.getProjectId())
        );

        User assignedTo = null;
        if (request.getAssignedTo() != null && !request.getAssignedTo().isBlank()) {
            assignedTo = userRepository.findByUsername(request.getAssignedTo())
                    .orElseThrow(() -> new UserNotFoundException(
                            "User not found " + request.getAssignedTo()
                    ));
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(TaskStatus.TODO)
                .priority(request.getPriority())
                .project(project)
                .assignedTo(assignedTo)
                .dueDate(request.getDueDate())
                .build();

        return TaskMapper.toTaskResponse(taskRepository.save(task));
    }

    @Override
    public TaskResponse getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(
                        "Task not found with ID " + taskId
                ));

        return TaskMapper.toTaskResponse(task);
    }

    @Override
    public List<TaskResponse> getTasksByProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException("Project not found with ID " + projectId);
        }

        return taskRepository.findByProjectId(projectId)
                .stream()
                .map(TaskMapper::toTaskResponse)
                .toList();
    }

    @Override
    public TaskResponse updateTaskStatus(Long taskId, String newStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(
                        "Task not found with ID " + taskId
                ));

        try {
            task.setStatus(TaskStatus.valueOf(newStatus.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new BusinessException(
                    "Invalid status '" + newStatus + "'. Valid values: TODO, IN_PROGRESS, DONE"
            );
        }

        return TaskMapper.toTaskResponse(task);
    }

    @Override
    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException("Task not found with ID " + taskId);
        }
        taskRepository.deleteById(taskId);
    }
}
