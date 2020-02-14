package com.bryan.ppmtool.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bryan.ppmtool.domain.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long>{

	@Override
	Iterable<Project> findAllById(Iterable<Long> ids);

	Project findByProjectIdentifier(String projectIdentifier);
}
