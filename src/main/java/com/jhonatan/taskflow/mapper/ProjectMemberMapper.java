package com.jhonatan.taskflow.mapper;

import com.jhonatan.taskflow.domain.entity.ProjectMember;
import com.jhonatan.taskflow.dto.ProjectMemberResponse;

public class ProjectMemberMapper {
    public static ProjectMemberResponse toProjectMemberResponse(ProjectMember projectMember) {
        if (projectMember == null) return null;

        return ProjectMemberResponse
                .builder()
                .username(projectMember.getUser().getUsername())
                .email(projectMember.getUser().getEmail())
                .role(projectMember.getRoleInProject())
                .joinedAt(projectMember.getJoinedAt())
                .build();
    }
}
