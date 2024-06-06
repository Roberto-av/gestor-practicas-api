package com.app.services.impl;

import com.app.controllers.dto.UserDTO;
import com.app.exceptions.IdNotFundException;
import com.app.exceptions.UniqueFieldViolationException;
import com.app.persistence.entities.users.UserEntity;
import com.app.persistence.repositories.RoleRepository;
import com.app.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserDTO> findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToDTO);
    }

    public UserDTO saveUser(UserDTO userDTO) {
        Optional<UserEntity> existingUserByUsername = userRepository.findByUsername(userDTO.getUsername());
        if (existingUserByUsername.isPresent()) {
            throw new UniqueFieldViolationException(userDTO.getUsername());
        }

        UserEntity user = new UserEntity();
        user.setUsername(userDTO.getUsername());

        if (userDTO.getRoles() != null) {
            user.setRoles(roleService.convertToEntitySet(userDTO.getRoles()));
        }

        user.setStudent(studentService.convertToEntity(userDTO.getStudent()));

        return convertToDTO(userRepository.save(user));
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new IdNotFundException(id));
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IdNotFundException(id));

        existingUser.setUsername(userDTO.getUsername());

        if (userDTO.getRoles() != null) {
            existingUser.setRoles(roleService.convertToEntitySet(userDTO.getRoles()));
        }

        existingUser.setStudent(studentService.convertToEntity(userDTO.getStudent()));

        return convertToDTO(userRepository.save(existingUser));
    }

    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IdNotFundException(id));
        userRepository.delete(user);
    }

    public UserDTO convertToDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setCreatedAt(userEntity.getCreatedAt());
        userDTO.setUpdatedAt(userEntity.getUpdatedAt());

        if (userEntity.getRoles() != null) {
            userDTO.setRoles(roleService.convertToDTOSet(userEntity.getRoles()));
        }

        if (userEntity.getStudent() != null) {
            userDTO.setStudent(studentService.convertToDTO(userEntity.getStudent()));
        }

        return userDTO;
    }

    public UserEntity convertToEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userDTO.getId());
        userEntity.setUsername(userDTO.getUsername());

        if (userDTO.getRoles() != null) {
            userEntity.setRoles(roleService.convertToEntitySet(userDTO.getRoles()));
        }

        if (userDTO.getStudent() != null) {
            userEntity.setStudent(studentService.convertToEntity(userDTO.getStudent()));
        }

        return userEntity;
    }
}
