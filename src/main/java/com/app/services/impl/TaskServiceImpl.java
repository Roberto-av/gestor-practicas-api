package com.app.services.impl;

import com.app.controllers.dto.FileDTO;
import com.app.controllers.dto.TaskDTO;
import com.app.exceptions.IdNotFundException;
import com.app.persistence.entities.groups.FileEntity;
import com.app.persistence.entities.groups.GroupEntity;
import com.app.persistence.entities.groups.TaskEntity;
import com.app.persistence.entities.users.UserEntity;
import com.app.persistence.repositories.FileRepository;
import com.app.persistence.repositories.GroupRepository;
import com.app.persistence.repositories.TaskRepository;
import com.app.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl {


    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private FileRepository fileRepository;

    public TaskDTO createTask(TaskDTO taskDTO) {
        TaskEntity taskEntity = convertToEntity(taskDTO);
        TaskEntity savedTask = taskRepository.save(taskEntity);
        return convertToDTO(savedTask);
    }

    public TaskDTO getTaskById(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new IdNotFundException(id));
        return convertToDTO(taskEntity);
    }

    public List<TaskDTO> getAllTasks() {
        List<TaskEntity> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        TaskEntity existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new IdNotFundException(id));

        existingTask.setTittle(taskDTO.getTittle());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setInitialDate(taskDTO.getInitialDate());
        existingTask.setEndDate(taskDTO.getEndDate());

        UserEntity user = userRepository.findById(taskDTO.getUserId())
                .orElseThrow(() -> new IdNotFundException(taskDTO.getUserId()));
        existingTask.setUser(user);

        GroupEntity group = groupRepository.findById(taskDTO.getGroupId())
                .orElseThrow(() -> new IdNotFundException(taskDTO.getGroupId()));
        existingTask.setGroup(group);

        TaskEntity savedTask = taskRepository.save(existingTask);
        return convertToDTO(savedTask);
    }

    public void deleteTask(Long id) {
        TaskEntity existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new IdNotFundException(id));
        taskRepository.delete(existingTask);
    }

    public FileDTO uploadFile(Long taskId, Long userId , MultipartFile file) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IdNotFundException(taskId));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IdNotFundException(userId));

        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        String fileStorageLocation = "src/main/resources/files/docs/";
        Path path = Paths.get(fileStorageLocation + uniqueFileName);

        try {
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path);

            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(originalFileName);
            fileEntity.setFile_path(path.toString());
            fileEntity.setUser(user);
            fileEntity.setTask(task);

            FileEntity savedFile = fileRepository.save(fileEntity);
            return convertToFileDTO(savedFile);
        } catch (IOException e) {
            throw new RuntimeException("Error al subir el archivo", e);
        }
    }

    public List<TaskDTO> getAllTasksWithFiles() {
        List<TaskEntity> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(taskEntity -> {
                    TaskDTO taskDTO = convertToDTO(taskEntity);
                    List<FileDTO> fileDTOs = taskEntity.getFiles().stream()
                            .map(this::convertToFileDTO)
                            .collect(Collectors.toList());
                    taskDTO.setFiles(fileDTOs);
                    return taskDTO;
                })
                .collect(Collectors.toList());
    }


    private TaskDTO convertToDTO(TaskEntity taskEntity) {
        return TaskDTO.builder()
                .id(taskEntity.getId())
                .tittle(taskEntity.getTittle())
                .description(taskEntity.getDescription())
                .initialDate(taskEntity.getInitialDate())
                .endDate(taskEntity.getEndDate())
                .userId(taskEntity.getUser().getId())
                .groupId(taskEntity.getGroup().getId())
                .build();
    }

    private TaskEntity convertToEntity(TaskDTO taskDTO) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(taskDTO.getId());
        taskEntity.setTittle(taskDTO.getTittle());
        taskEntity.setDescription(taskDTO.getDescription());
        taskEntity.setInitialDate(taskDTO.getInitialDate());
        taskEntity.setEndDate(taskDTO.getEndDate());

        UserEntity user = userRepository.findById(taskDTO.getUserId())
                .orElseThrow(() -> new IdNotFundException(taskDTO.getUserId()));
        taskEntity.setUser(user);

        GroupEntity group = groupRepository.findById(taskDTO.getGroupId())
                .orElseThrow(() -> new IdNotFundException(taskDTO.getGroupId()));
        taskEntity.setGroup(group);

        return taskEntity;
    }

    private FileDTO convertToFileDTO(FileEntity fileEntity) {
        return FileDTO.builder()
                .id(fileEntity.getId())
                .name(fileEntity.getName())
                .filePath(fileEntity.getFile_path())
                .taskId(fileEntity.getTask().getId())
                .userId(fileEntity.getUser().getId())
                .build();
    }
}
