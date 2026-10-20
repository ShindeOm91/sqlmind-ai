package com.sqlmind.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Project> createProject(@RequestBody Project project,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        project.setUser(user);

        Project savedProject = projectService.createProject(project);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
    }
	
	@GetMapping
	public ResponseEntity<List<Project>> getProjects(
	        Authentication authentication) {

	    String email = authentication.getName();

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    List<Project> projects =
	            projectService.getProjectsByUserId(user.getId());

	    return ResponseEntity.ok(projects);
	}
}
