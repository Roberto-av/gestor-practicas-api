package com.app.services.impl;

import com.app.controllers.dto.GroupDTO;
import com.app.exceptions.IdNotFundException;
import com.app.persistence.entities.groups.GroupEntity;
import com.app.persistence.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl {

    @Autowired
    private GroupRepository groupRepository;

    public GroupDTO createGroup(GroupDTO groupDTO) {
        GroupEntity groupEntity = convertToEntity(groupDTO);
        GroupEntity savedGroup = groupRepository.save(groupEntity);
        return convertToDTO(savedGroup);
    }

    public GroupDTO getGroupById(Long id) {
        GroupEntity groupEntity = groupRepository.findById(id)
                .orElseThrow(() -> new IdNotFundException(id));
        return convertToDTO(groupEntity);
    }

    public List<GroupDTO> getAllGroups() {
        List<GroupEntity> groups = groupRepository.findAll();
        return groups.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public GroupDTO updateGroup(Long id, GroupDTO groupDTO) {
        GroupEntity existingGroup = groupRepository.findById(id)
                .orElseThrow(() -> new IdNotFundException(id));

        existingGroup.setName(groupDTO.getName());
        existingGroup.setDescription(groupDTO.getDescription());

        GroupEntity savedGroup = groupRepository.save(existingGroup);
        return convertToDTO(savedGroup);
    }

    public void deleteGroup(Long id) {
        GroupEntity existingGroup = groupRepository.findById(id)
                .orElseThrow(() -> new IdNotFundException(id));
        groupRepository.delete(existingGroup);
    }

    private GroupDTO convertToDTO(GroupEntity groupEntity) {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(groupEntity.getId());
        groupDTO.setName(groupEntity.getName());
        groupDTO.setDescription(groupEntity.getDescription());
        return groupDTO;
    }

    private GroupEntity convertToEntity(GroupDTO groupDTO) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(groupDTO.getId());
        groupEntity.setName(groupDTO.getName());
        groupEntity.setDescription(groupDTO.getDescription());
        return groupEntity;
    }
}
