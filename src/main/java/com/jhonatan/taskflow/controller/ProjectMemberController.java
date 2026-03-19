package com.jhonatan.taskflow.controller;

import com.jhonatan.taskflow.dto.ProjectMemberRequest;
import com.jhonatan.taskflow.dto.ProjectMemberResponse;
import com.jhonatan.taskflow.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/taskflow/projects/{projectId}/members")
@RequiredArgsConstructor
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;

    @PostMapping
    public ResponseEntity<ProjectMemberResponse> addMember(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectMemberRequest request
    ) {
        return ResponseEntity.ok(projectMemberService.addMember(projectId, request));
    }

    @GetMapping
    public ResponseEntity<List<ProjectMemberResponse>> getMembers(
            @PathVariable Long projectId
    ) {
        return ResponseEntity.ok(projectMemberService.getMembers(projectId));
    }

    @PutMapping
    public ResponseEntity<ProjectMemberResponse> updateMemberRole(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectMemberRequest request
    ) {
        return ResponseEntity.ok(projectMemberService.updateMemberRole(projectId, request));
    }

    @DeleteMapping
    public ResponseEntity<Void> removeMember(
            @PathVariable Long projectId,
            @RequestParam String username
    ) {
        projectMemberService.removeMember(projectId, username);
        return ResponseEntity.noContent().build();
    }
}
