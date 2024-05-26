package com.app.seeders;

import com.app.controllers.dto.InstitutionDTO;
import com.app.persistence.entities.institutions.InstitutionEntity;
import com.app.persistence.entities.institutions.ProjectEntity;
import com.app.persistence.repositories.InstitutionRepository;
import com.app.persistence.repositories.ProjectRepository;
import com.app.services.IInsitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateProjects {

    private final ProjectRepository projectRepository;
    private final IInsitutionService insitutionService;

    @Autowired
    public CreateProjects(ProjectRepository projectRepository, IInsitutionService insitutionService) {
        this.projectRepository = projectRepository;
        this.insitutionService = insitutionService;
    }

    public void createAllProjects() {

        InstitutionEntity institution1 =  insitutionService.getInstitutionEntityById(1L);

        ProjectEntity project1 = ProjectEntity.builder()
                .name("Tronja lab")
                .schedule("Lunes a Viernes de 9:00 a 17:00")
                .justification("justi 1")
                .objectives("enseñar bien al estudiante")
                .functions("Desarrollador web")
                .numHours(40)
                .requested(5)
                .registered(3)
                .institution(institution1)
                .build();

        projectRepository.saveAll(List.of(project1));
    }
}
