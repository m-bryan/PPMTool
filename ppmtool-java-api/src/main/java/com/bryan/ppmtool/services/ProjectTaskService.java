package com.bryan.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bryan.ppmtool.domain.Backlog;
import com.bryan.ppmtool.domain.Project;
import com.bryan.ppmtool.domain.ProjectTask;
import com.bryan.ppmtool.exceptions.ProjectNotFoundException;
import com.bryan.ppmtool.repositories.BacklogRepository;
import com.bryan.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private BacklogRepository backlogRepository;

	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	@Autowired
	private ProjectService projectService;

	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
		
		Project project = projectService.findProjectByProjectId(projectIdentifier);
		
		Backlog backlog = backlogRepository.findByProjectIdentifier(project.getProjectIdentifier());
		projectTask.setBacklog(backlog);

		Integer backlogSequence = backlog.getPTSequence();
		backlogSequence++;
		backlog.setPTSequence(backlogSequence);

		projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
		projectTask.setProjectIdentifier(projectIdentifier);

		if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
			projectTask.setPriority(3);
		}

		if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
			projectTask.setStatus("TO_DO");
		}

		return projectTaskRepository.save(projectTask);
	}
	
	public Iterable<ProjectTask> findBacklogById(String id) {
		Project project = projectService.findProjectByProjectId(id);
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(project.getProjectIdentifier());
	}
	
	public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id) {
		
		Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
		if(backlog == null) throw new ProjectNotFoundException("Project with ID '" + backlog_id +"' does not exist");
		
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
		if(projectTask == null) throw new ProjectNotFoundException("Project task '" + pt_id +"' does not exist");
		
		if(!projectTask.getProjectIdentifier().equals(backlog.getProjectIdentifier())) 
			throw new ProjectNotFoundException("Project task '" + pt_id +"' does not exist in project '" + backlog_id +"'");
		
		return projectTask;
	}
	
	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id) {
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
		projectTask = updatedTask;
		return projectTaskRepository.save(projectTask);
	}

}
