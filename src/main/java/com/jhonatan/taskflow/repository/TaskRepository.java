package com.jhonatan.taskflow.repository;

import com.jhonatan.taskflow.domain.entity.Task;
import com.jhonatan.taskflow.domain.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByProjectId(Long projectId, Pageable pageable);

    List<Task> findByProjectId(Long projectId);

    List<Task> findByAssignedToId(Long userId);

    List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status);
}
