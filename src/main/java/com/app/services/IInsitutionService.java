package com.app.services;

import com.app.controllers.dto.InstitutionDTO;
import com.app.persistence.entities.institutions.InstitutionEntity;

import java.util.List;

public interface IInsitutionService {

    InstitutionDTO createInstitution(InstitutionDTO institutionDTO);

    InstitutionDTO getInstitutionById(Long id);

    InstitutionEntity getInstitutionEntityById(Long id);

    List<InstitutionDTO> getAllInstitutions();

    InstitutionDTO updateInstitution(Long id, InstitutionDTO institutionDTO);

    void deleteInstitution(Long id);
}
