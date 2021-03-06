package com.bryan.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bryan.ppmtool.domain.Backlog;
import com.bryan.ppmtool.domain.Project;
import com.bryan.ppmtool.exceptions.ProjectIdException;
import com.bryan.ppmtool.repositories.BacklogRepository;
import com.bryan.ppmtool.repositories.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;

	public Project saveOrUpdateProject(Project project) {
		try {
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			
			if(project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			}else {
				Backlog backlog = backlogRepository.findByProjectIdentifier(project.getProjectIdentifier());
				project.setBacklog(backlog);
			}
			
			return projectRepository.save(project);
		} catch (Exception e) {
			throw new ProjectIdException(
					"Project ID " + project.getProjectIdentifier().toUpperCase() + " already exists");
		}
		
	}

	public Project findProjectByProjectId(String projectIdentifier) {
		Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
		if (project == null) {
			throw new ProjectIdException("Project ID " + projectIdentifier.toUpperCase() + " does not exists");
		}

		return project;
	}

	public Iterable<Project> findAllProjects() {
		return projectRepository.findAll();
	}
	
	public void deleteProjectByIdentifier(String projectIdentifier) {
		Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
		if (project == null) {
			throw new ProjectIdException("Cannot delete Project with ID " + projectIdentifier.toUpperCase() + ". does not exists");
		}

		projectRepository.delete(project);
	}

}
