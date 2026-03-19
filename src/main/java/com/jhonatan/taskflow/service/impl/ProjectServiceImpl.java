package com.jhonatan.taskflow.service.impl;

import com.jhonatan.taskflow.domain.entity.Project;
import com.jhonatan.taskflow.domain.entity.ProjectMember;
import com.jhonatan.taskflow.domain.entity.User;
import com.jhonatan.taskflow.domain.enums.ProjectRole;
import com.jhonatan.taskflow.dto.ProjectCreateRequest;
import com.jhonatan.taskflow.dto.ProjectResponse;
import com.jhonatan.taskflow.exception.ProjectNotFoundException;
import com.jhonatan.taskflow.exception.UserNotFoundException;
import com.jhonatan.taskflow.mapper.ProjectMapper;
import com.jhonatan.taskflow.repository.ProjectMemberRepository;
import com.jhonatan.taskflow.repository.ProjectRepository;
import com.jhonatan.taskflow.repository.UserRepository;
import com.jhonatan.taskflow.security.CustomUserDetails;
import com.jhonatan.taskflow.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;

    @Override
    public ProjectResponse createProject(ProjectCreateRequest request) {

        User creator = getCurrentUser();

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdBy(creator)
                .build();

        Project savedProject = projectRepository.save(project);

        ProjectMember projectMember = ProjectMember.builder()
                .project(project)
                .user(creator)
                .roleInProject(ProjectRole.LEADER)
                .build();

        projectMemberRepository.save(projectMember);

        return ProjectResponse.builder()
                .id(savedProject.getId())
                .name(savedProject.getName())
                .description(savedProject.getDescription())
                .createdBy(creator.getUsername())
                .createdAt(savedProject.getCreatedAt())
                .build();
    }

    @Override
    public ProjectResponse getProjectById(long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException("Project not found with ID " + projectId)
        );
        return ProjectMapper.toProjectResponse(project);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("No authenticated user found");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }

}
