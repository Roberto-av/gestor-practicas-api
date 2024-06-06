package com.app.services.impl;

import com.app.controllers.dto.RoleDTO;
import com.app.exceptions.IdNotFundException;
import com.app.persistence.entities.users.RoleEntity;
import com.app.persistence.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl {

    @Autowired
    private RoleRepository roleRepository;

    public RoleDTO convertToDTO(RoleEntity roleEntity) {
        return new RoleDTO(roleEntity.getId(), roleEntity.getRoleEnum().name());
    }

    public RoleEntity convertToEntity(RoleDTO roleDTO) {
        return roleRepository.findById(roleDTO.getId())
                .orElseThrow(() -> new IdNotFundException(roleDTO.getId()));
    }

    public Set<RoleDTO> convertToDTOSet(Set<RoleEntity> roleEntities) {
        return roleEntities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toSet());
    }

    public Set<RoleEntity> convertToEntitySet(Set<RoleDTO> roleDTOs) {
        return roleDTOs.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toSet());
    }
}
