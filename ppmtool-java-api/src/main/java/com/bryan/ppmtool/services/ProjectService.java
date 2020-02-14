package com.bryan.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bryan.ppmtool.domain.Project;
import com.bryan.ppmtool.exceptions.ProjectIdException;
import com.bryan.ppmtool.repositories.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	public Project saveOrUpdateProject(Project project) {
		try {
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			return projectRepository.save(project);
		} catch (Exception e) {
			throw new ProjectIdException(
					"Prject ID " + project.getProjectIdentifier().toUpperCase() + " already exists");
		}

	}

	public Project findProjectByProjectId(String projectIdentifier) {
		Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
		if (project == null) {
			throw new ProjectIdException("Prject ID " + projectIdentifier.toUpperCase() + " does not exists");
		}

		return project;
	}

}
