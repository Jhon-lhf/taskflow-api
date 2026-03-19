package com.jhonatan.taskflow.dto;

import com.jhonatan.taskflow.domain.enums.ProjectRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProjectMemberRequest {
    @NotBlank(message = "Username must not be empty")
    private String username;

    @NotNull(message = "Role is required")
    private ProjectRole role;
}
