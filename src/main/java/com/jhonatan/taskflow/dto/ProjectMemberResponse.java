package com.jhonatan.taskflow.dto;

import com.jhonatan.taskflow.domain.enums.ProjectRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProjectMemberResponse {
    private String username;
    private String email;
    private ProjectRole role;
    private LocalDateTime joinedAt;
}
