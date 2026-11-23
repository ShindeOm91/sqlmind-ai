package com.sqlmind.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sqlmind.dto.response.ProjectResponse;
import com.sqlmind.entity.Project;
import com.sqlmind.entity.User;
import com.sqlmind.repository.UserRepository;
import com.sqlmind.service.ProjectService;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

	private final ProjectService projectService;
	
	private final UserRepository userRepository;
	
	public ProjectController(
	        ProjectService projectService,
	        UserRepository userRepository) {

	    this.projectService = projectService;
	    this.userRepository = userRepository;
	}
	
	@PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody Project project,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        project.setUser(user);

        Project savedProject = projectService.createProject(project);

        ProjectResponse response = new ProjectResponse(
                savedProject.getId(),
                savedProject.getName(),
                savedProject.getDescription(),
                savedProject.getCreatedAt(),
                savedProject.getUpdatedAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
	
	@GetMapping
	public ResponseEntity<List<ProjectResponse>> getProjects(
	        Authentication authentication) {

	    String email = authentication.getName();

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    List<Project> projects =
	            projectService.getProjectsByUserId(user.getId());

	    List<ProjectResponse> response = projects.stream()
	            .map(project -> new ProjectResponse(
	                    project.getId(),
	                    project.getName(),
	                    project.getDescription(),
	                    project.getCreatedAt(),
	                    project.getUpdatedAt()
	            ))
	            .toList();

	    return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ProjectResponse> updateProject(
	        @PathVariable Long id,
	        @RequestBody Project projectRequest,
	        Authentication authentication) {

	    User user = userRepository.findByEmail(authentication.getName())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    Project updatedProject = projectService.updateProject(
	            id,
	            user.getId(),
	            projectRequest.getName(),
	            projectRequest.getDescription()
	    );

	    ProjectResponse response = new ProjectResponse(
	            updatedProject.getId(),
	            updatedProject.getName(),
	            updatedProject.getDescription(),
	            updatedProject.getCreatedAt(),
	            updatedProject.getUpdatedAt()
	    );

	    return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProject(
	        @PathVariable Long id,
	        Authentication authentication) {

	    User user = userRepository.findByEmail(authentication.getName())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    projectService.deleteProject(id, user.getId());

	    return ResponseEntity.noContent().build();
	}
}
