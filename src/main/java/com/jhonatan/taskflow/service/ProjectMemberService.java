package com.jhonatan.taskflow.service;

import com.jhonatan.taskflow.dto.ProjectMemberRequest;
import com.jhonatan.taskflow.dto.ProjectMemberResponse;

import java.util.List;

public interface ProjectMemberService {
    ProjectMemberResponse addMember(Long projectId, ProjectMemberRequest request);

    List<ProjectMemberResponse> getMembers(Long projectId);

    ProjectMemberResponse updateMemberRole(Long projectId, ProjectMemberRequest request);

    void removeMember(Long projectId, String username);
}