package com.jhonatan.taskflow.service;

import com.jhonatan.taskflow.dto.ProjectCreateRequest;
import com.jhonatan.taskflow.dto.ProjectResponse;

public interface ProjectService {
    ProjectResponse createProject(ProjectCreateRequest request);

    ProjectResponse getProjectById(long projectId);
}
