package com.app.services.impl;

import com.app.controllers.dto.AddressDTO;
import com.app.controllers.dto.InstitutionDTO;
import com.app.controllers.dto.ResponsibleDTO;
import com.app.exceptions.IdNotFundException;
import com.app.exceptions.UniqueFieldViolationException;
import com.app.persistence.entities.institutions.AddressEntity;
import com.app.persistence.entities.institutions.InstitutionEntity;
import com.app.persistence.entities.institutions.ResponsibleEntity;
import com.app.persistence.entities.students.StudentEntity;
import com.app.persistence.repositories.InstitutionRepository;
import com.app.services.IInsitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InstitutionService implements IInsitutionService {

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private AddressServiceImpl addressService;

    @Autowired
    private ResponsibleServiceImpl responsibleService;


    @Override
    public InstitutionDTO createInstitution(InstitutionDTO institutionDTO) {

        Optional<InstitutionEntity> existingInstitutionByRfc = institutionRepository.findInstitutionByRfc(institutionDTO.getRfc());
        if (existingInstitutionByRfc.isPresent()) {
            throw new UniqueFieldViolationException(institutionDTO.getRfc());
        }

        Optional<InstitutionEntity> existingInstitutionByCompanyName = institutionRepository.findInstitutionByCompanyName(institutionDTO.getCompanyName());
        if (existingInstitutionByCompanyName.isPresent()) {
            throw new UniqueFieldViolationException(institutionDTO.getCompanyName());
        }

        InstitutionEntity institutionEntity = convertToEntity(institutionDTO);
        InstitutionEntity savedInstitution = institutionRepository.save(institutionEntity);

        return convertToDTO(savedInstitution);
    }

    @Override
    public InstitutionDTO getInstitutionById(Long id) {
        InstitutionEntity institutionEntity = institutionRepository
                .findById(id)
                .orElseThrow(() -> new IdNotFundException(id));

        return convertToDTO(institutionEntity);
    }

    @Override
    public InstitutionEntity getInstitutionEntityById(Long id) {
        return institutionRepository
                .findById(id)
                .orElseThrow(() -> new IdNotFundException(id));
    }

    @Override
    public List<InstitutionDTO> getAllInstitutions() {
        List<InstitutionEntity> institutions = institutionRepository.findAll();
        return institutions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public InstitutionDTO updateInstitution(Long id, InstitutionDTO institutionDTO) {

        InstitutionEntity existingInstitution = institutionRepository.findById(id)
                .orElseThrow(() -> new IdNotFundException(id));

        existingInstitution.setName(institutionDTO.getName());
        existingInstitution.setRfc(institutionDTO.getRfc());
        existingInstitution.setCompanyName(institutionDTO.getCompanyName());
        existingInstitution.setGiro(institutionDTO.getGiro());
        existingInstitution.setWeb(institutionDTO.getWeb());
        existingInstitution.setSupport(institutionDTO.isSupport());
        existingInstitution.setSector(institutionDTO.getSector());
        existingInstitution.setModality(institutionDTO.getModality());
        existingInstitution.setTelephoneNumber(institutionDTO.getTelephoneNumber());
        existingInstitution.setStatus(institutionDTO.getStatus());


        if (institutionDTO.getAddress() != null) {
            AddressEntity addressEntity = addressService.convertToEntity(institutionDTO.getAddress());
            existingInstitution.setAddress(addressEntity);
        }

        if (institutionDTO.getResponsible() != null) {
            ResponsibleEntity responsibleEntity = responsibleService.convertToEntity(institutionDTO.getResponsible());
            existingInstitution.setResponsible(responsibleEntity);
        }

        InstitutionEntity savedInstitution = institutionRepository.save(existingInstitution);

        return convertToDTO(savedInstitution);
    }

    @Override
    public void deleteInstitution(Long id) {
        InstitutionEntity existingInstitution = institutionRepository.findById(id)
                .orElseThrow(() -> new IdNotFundException(id));
        institutionRepository.delete(existingInstitution);
    }

    public InstitutionDTO convertToDTO(InstitutionEntity institutionEntity) {
        InstitutionDTO institutionDTO = new InstitutionDTO();
        institutionDTO.setId(institutionEntity.getId());
        institutionDTO.setName(institutionEntity.getName());
        institutionDTO.setRfc(institutionEntity.getRfc());
        institutionDTO.setCompanyName(institutionEntity.getCompanyName());
        institutionDTO.setGiro(institutionEntity.getGiro());
        institutionDTO.setWeb(institutionEntity.getWeb());
        institutionDTO.setSupport(institutionEntity.isSupport());
        institutionDTO.setSector(institutionEntity.getSector());
        institutionDTO.setModality(institutionEntity.getModality());
        institutionDTO.setStatus(institutionEntity.getStatus());
        institutionDTO.setTelephoneNumber(institutionEntity.getTelephoneNumber());

        // Convertir la dirección si existe
        if (institutionEntity.getAddress() != null) {
            AddressDTO addressDTO = addressService.convertToDTO(institutionEntity.getAddress());
            institutionDTO.setAddress(addressDTO);
        }

        // Convertir el responsable si existe
        if (institutionEntity.getResponsible() != null) {
            ResponsibleDTO responsibleDTO = responsibleService.convertToDTO(institutionEntity.getResponsible());
            institutionDTO.setResponsible(responsibleDTO);
        }

        return institutionDTO;
    }


    public InstitutionEntity convertToEntity(InstitutionDTO institutionDTO) {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(institutionDTO.getId());
        institutionEntity.setName(institutionDTO.getName());
        institutionEntity.setRfc(institutionDTO.getRfc());
        institutionEntity.setCompanyName(institutionDTO.getCompanyName());
        institutionEntity.setGiro(institutionDTO.getGiro());
        institutionEntity.setWeb(institutionDTO.getWeb());
        institutionEntity.setSupport(institutionDTO.isSupport());
        institutionEntity.setSector(institutionDTO.getSector());
        institutionEntity.setModality(institutionDTO.getModality());
        institutionEntity.setStatus(institutionDTO.getStatus());
        institutionEntity.setTelephoneNumber(institutionDTO.getTelephoneNumber());

        // Convertir la dirección si existe
        if (institutionDTO.getAddress() != null) {
            AddressEntity addressEntity = addressService.convertToEntity(institutionDTO.getAddress());
            institutionEntity.setAddress(addressEntity);
        }

        // Convertir el responsable si existe
        if (institutionDTO.getResponsible() != null) {
            ResponsibleEntity responsibleEntity = responsibleService.convertToEntity(institutionDTO.getResponsible());
            institutionEntity.setResponsible(responsibleEntity);
        }

        return institutionEntity;

    }

}

