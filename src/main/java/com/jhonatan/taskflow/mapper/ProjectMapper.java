package com.jhonatan.taskflow.mapper;

import com.jhonatan.taskflow.domain.entity.Project;
import com.jhonatan.taskflow.dto.ProjectResponse;

public class ProjectMapper {
    public static ProjectResponse toProjectResponse(Project project) {
        if (project == null) return null;

        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .createdBy(project.getCreatedBy().getUsername())
                .createdAt(project.getCreatedAt())
                .build();
    }
}
