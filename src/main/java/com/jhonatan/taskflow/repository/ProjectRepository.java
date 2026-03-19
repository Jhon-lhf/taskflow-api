package com.jhonatan.taskflow.repository;

import com.jhonatan.taskflow.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
