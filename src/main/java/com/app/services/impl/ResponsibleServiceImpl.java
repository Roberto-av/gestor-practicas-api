package com.app.services.impl;

import com.app.controllers.dto.ResponsibleDTO;
import com.app.exceptions.IdNotFundException;
import com.app.persistence.entities.institutions.ResponsibleEntity;
import com.app.persistence.repositories.ResponsibleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResponsibleServiceImpl {

    @Autowired
    private ResponsibleRepository responsibleRepository;

    public ResponsibleDTO createResponsible(ResponsibleDTO responsibleDTO) {
        ResponsibleEntity responsibleEntity = convertToEntity(responsibleDTO);
        responsibleEntity = responsibleRepository.save(responsibleEntity);
        return convertToDTO(responsibleEntity);
    }

    public ResponsibleDTO getResponsibleById(Long id) {
        return responsibleRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new IdNotFundException(id));
    }

    public List<ResponsibleDTO> getResponsibles() {
        return responsibleRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ResponsibleDTO updateResponsible(Long id, ResponsibleDTO responsibleDTO) {
        if (responsibleDTO == null) {
            throw new IllegalArgumentException("No se encontro el id en la solicitud");
        }

        ResponsibleDTO existingResponsible = getResponsibleById(id);

        return responsibleRepository.findById(id)
                .map(responsible -> {
                    responsible.setName(responsibleDTO.getName());
                    responsible.setPosition(responsibleDTO.getPosition());
                    responsible.setEmail(responsibleDTO.getEmail());
                    responsible.setConfirmEmail(responsibleDTO.getConfirmEmail());
                    responsible.setPhone(responsibleDTO.getPhone());
                    responsible.setEducation(responsibleDTO.getEducation());
                    return convertToDTO(responsibleRepository.save(responsible));
                }).orElseGet(() -> {
                    ResponsibleEntity responsibleEntity = convertToEntity(responsibleDTO);
                    responsibleEntity.setId(id);
                    return convertToDTO(responsibleRepository.save(responsibleEntity));
                });
    }

    public ResponsibleDTO convertToDTO(ResponsibleEntity responsibleEntity) {
        ResponsibleDTO responsibleDTO = new ResponsibleDTO();
        responsibleDTO.setId(responsibleEntity.getId());
        responsibleDTO.setName(responsibleEntity.getName());
        responsibleDTO.setPosition(responsibleEntity.getPosition());
        responsibleDTO.setEmail(responsibleEntity.getEmail());
        responsibleDTO.setConfirmEmail(responsibleEntity.getConfirmEmail());
        responsibleDTO.setPhone(responsibleEntity.getPhone());
        responsibleDTO.setEducation(responsibleEntity.getEducation());
        return responsibleDTO;
    }

    public ResponsibleEntity convertToEntity(ResponsibleDTO responsibleDTO) {
        ResponsibleEntity responsibleEntity = new ResponsibleEntity();
        responsibleEntity.setId(responsibleDTO.getId());
        responsibleEntity.setName(responsibleDTO.getName());
        responsibleEntity.setPosition(responsibleDTO.getPosition());
        responsibleEntity.setEmail(responsibleDTO.getEmail());
        responsibleEntity.setConfirmEmail(responsibleDTO.getConfirmEmail());
        responsibleEntity.setPhone(responsibleDTO.getPhone());
        responsibleEntity.setEducation(responsibleDTO.getEducation());
        return responsibleEntity;
    }
}
