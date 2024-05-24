package com.app.services.impl;

import com.app.controllers.dto.ProjectDTO;
import com.app.exceptions.IdNotFundException;
import com.app.persistence.entities.institutions.InstitutionEntity;
import com.app.persistence.entities.institutions.ProjectEntity;
import com.app.persistence.entities.institutions.ResponsibleEntity;
import com.app.persistence.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ResponsibleServiceImpl responsibleService;

    @Autowired
    private InstitutionService institutionService;

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        ProjectEntity projectEntity = convertToEntity(projectDTO);
        ProjectEntity savedProject = projectRepository.save(projectEntity);
        return convertToDTO(savedProject);
    }

    public ProjectDTO getProjectById(Long id) {
        ProjectEntity projectEntity = projectRepository.findById(id)
                .orElseThrow(() -> new IdNotFundException(id));
        return convertToDTO(projectEntity);
    }

    public List<ProjectDTO> getAllProjects() {
        List<ProjectEntity> projects = projectRepository.findAll();
        return projects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        ProjectEntity existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new IdNotFundException(id));

        existingProject.setName(projectDTO.getName());
        existingProject.setSchedule(projectDTO.getSchedule());
        existingProject.setJustification(projectDTO.getJustification());
        existingProject.setObjectives(projectDTO.getObjectives());
        existingProject.setFunctions(projectDTO.getFunctions());
        existingProject.setNumHours(projectDTO.getNumHours());
        existingProject.setRequested(projectDTO.getRequested());
        existingProject.setRegistered(projectDTO.getRegistered());

        InstitutionEntity institutionEntity = institutionService.convertToEntity(institutionService.getInstitutionById(projectDTO.getInstitutionId()));
        existingProject.setInstitution(institutionEntity);

        ProjectEntity savedProject = projectRepository.save(existingProject);
        return convertToDTO(savedProject);
    }

    public void deleteProject(Long id) {
        ProjectEntity existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new IdNotFundException(id));
        projectRepository.delete(existingProject);
    }

    private ProjectDTO convertToDTO(ProjectEntity projectEntity) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(projectEntity.getId());
        projectDTO.setName(projectEntity.getName());
        projectDTO.setSchedule(projectEntity.getSchedule());
        projectDTO.setJustification(projectEntity.getJustification());
        projectDTO.setObjectives(projectEntity.getObjectives());
        projectDTO.setFunctions(projectEntity.getFunctions());
        projectDTO.setNumHours(projectEntity.getNumHours());
        projectDTO.setRequested(projectEntity.getRequested());
        projectDTO.setRegistered(projectEntity.getRegistered());
        projectDTO.setInstitutionId(projectEntity.getInstitution().getId());
        return projectDTO;
    }

    private ProjectEntity convertToEntity(ProjectDTO projectDTO) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(projectDTO.getId());
        projectEntity.setName(projectDTO.getName());
        projectEntity.setSchedule(projectDTO.getSchedule());
        projectEntity.setJustification(projectDTO.getJustification());
        projectEntity.setObjectives(projectDTO.getObjectives());
        projectEntity.setFunctions(projectDTO.getFunctions());
        projectEntity.setNumHours(projectDTO.getNumHours());
        projectEntity.setRequested(projectDTO.getRequested());
        projectEntity.setRegistered(projectDTO.getRegistered());

        InstitutionEntity institutionEntity = institutionService.getInstitutionEntityById(projectDTO.getInstitutionId());
        projectEntity.setInstitution(institutionEntity);

        return projectEntity;
    }
}
