package com.jhonatan.taskflow.service.impl;

import com.jhonatan.taskflow.domain.entity.Project;
import com.jhonatan.taskflow.domain.entity.ProjectMember;
import com.jhonatan.taskflow.domain.entity.User;
import com.jhonatan.taskflow.domain.enums.ProjectRole;
import com.jhonatan.taskflow.dto.ProjectMemberRequest;
import com.jhonatan.taskflow.dto.ProjectMemberResponse;
import com.jhonatan.taskflow.exception.MemberAlreadyExistsException;
import com.jhonatan.taskflow.exception.ProjectNotFoundException;
import com.jhonatan.taskflow.exception.UnauthorizedProjectActionException;
import com.jhonatan.taskflow.exception.UserNotFoundException;
import com.jhonatan.taskflow.mapper.ProjectMemberMapper;
import com.jhonatan.taskflow.repository.ProjectMemberRepository;
import com.jhonatan.taskflow.repository.ProjectRepository;
import com.jhonatan.taskflow.repository.UserRepository;
import com.jhonatan.taskflow.security.CustomUserDetails;
import com.jhonatan.taskflow.service.ProjectMemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public ProjectMemberResponse addMember(Long projectId, ProjectMemberRequest request) {
        requireLeaderRole(projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(
                        "Project not found with ID" + projectId
                ));

        User userToAdd = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found " + request.getUsername()
                ));

        if (projectMemberRepository.existsByProjectIdAndUserId(projectId, userToAdd.getId())) {
            throw new MemberAlreadyExistsException(
                    "User '" + request.getUsername() + "' is already a member of this project"
            );
        }

        ProjectMember projectMember = ProjectMember
                .builder()
                .project(project)
                .user(userToAdd)
                .roleInProject(request.getRole())
                .build();

        ProjectMember saved = projectMemberRepository.save(projectMember);

        return ProjectMemberMapper.toProjectMemberResponse(saved);
    }

    @Override
    public List<ProjectMemberResponse> getMembers(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException(
                    "Project not found wiht ID" + projectId
            );
        }

        return projectMemberRepository.findByProjectId(projectId)
                .stream()
                .map(ProjectMemberMapper::toProjectMemberResponse)
                .toList();
    }

    @Override
    public ProjectMemberResponse updateMemberRole(Long projectId, ProjectMemberRequest request) {
        requireLeaderRole(projectId);

        User targetUser = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found " + request.getUsername()
                ));

        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, targetUser.getId())
                .orElseThrow(() -> new UnauthorizedProjectActionException(
                        "User '" + targetUser.getUsername() + "' is not a member of this project"
                ));

        // No se puede cambiar el rol del único LEADER — el proyecto quedaría sin líder
        if (member.getRoleInProject() == ProjectRole.LEADER) {
            long leaderCount = projectMemberRepository.findByProjectId(projectId)
                    .stream()
                    .filter(m -> m.getRoleInProject() == ProjectRole.LEADER)
                    .count();

            if (leaderCount == 1) {
                throw new UnauthorizedProjectActionException(
                        "Cannot change role of the only LEADER. Assign another LEADER first."
                );
            }
        }

        member.setRoleInProject(request.getRole());

        return ProjectMemberMapper.toProjectMemberResponse(member);
    }

    @Override
    public void removeMember(Long projectId, String username) {
        requireLeaderRole(projectId);

        User targetUser = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found " + username
                ));

        ProjectMember member = projectMemberRepository
                .findByProjectIdAndUserId(projectId, targetUser.getId())
                .orElseThrow(() -> new UserNotFoundException(
                        "User '" + username + "' is not a member of this project"
                ));

        if (member.getRoleInProject() == ProjectRole.LEADER) {
            long leaderCount = projectMemberRepository
                    .findByProjectId(projectId)
                    .stream()
                    .filter(m -> m.getRoleInProject() == ProjectRole.LEADER)
                    .count();

            if (leaderCount == 1) {
                throw new UnauthorizedProjectActionException(
                        "Cannot remove the only LEADER of the project."
                );
            }
        }

        projectMemberRepository.delete(member);

    }

    private void requireLeaderRole(Long projectId) {
        User currentUser = getCurrentUser();

        ProjectMember currentMember = projectMemberRepository.findByProjectIdAndUserId(projectId, currentUser.getId())
                .orElseThrow(() -> new UnauthorizedProjectActionException(
                        "You are not a member of this project."
                ));

        if (currentMember.getRoleInProject() != ProjectRole.LEADER) {
            throw new UnauthorizedProjectActionException(
                    "Only the project LEADER can perform this action."
            );
        }

    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return Objects.requireNonNull(userDetails).getUser();
    }

}
