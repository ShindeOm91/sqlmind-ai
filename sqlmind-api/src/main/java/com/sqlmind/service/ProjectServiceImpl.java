package com.sqlmind.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sqlmind.entity.Project;
import com.sqlmind.repository.ProjectRepository;

@Service
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;
	
	public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
	
	@Override
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }
	
	@Override
	public List<Project> getProjectsByUserId(Long userId) {
	    return projectRepository.findByUserId(userId);
	}
	
	@Override
	public Project updateProject(Long projectId, Long userId, String name, String description) {

	    Project project = projectRepository.findByIdAndUserId(projectId, userId)
	            .orElseThrow(() -> new RuntimeException("Project not found"));

	    project.setName(name);
	    project.setDescription(description);

	    return projectRepository.save(project);
	}
	
	@Override
	public void deleteProject(Long projectId, Long userId) {

	    Project project = projectRepository.findByIdAndUserId(projectId, userId)
	            .orElseThrow(() -> new RuntimeException("Project not found"));

	    projectRepository.delete(project);
	}
}
