package com.jhonatan.taskflow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ProjectCreateRequest {
    @NotBlank(message = "The name can't be blank")
    private String name;

    private String description;
}
