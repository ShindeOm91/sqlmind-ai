package com.sqlmind.service;

import java.util.List;

import com.sqlmind.entity.Project;

public interface ProjectService {

	Project createProject(Project project);

    List<Project> getProjectsByUserId(Long userId);
}
