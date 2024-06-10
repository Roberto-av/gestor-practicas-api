package com.app.services.impl;

import com.app.components.ConverterDTO;
import com.app.controllers.dto.FileDTO;
import com.app.exceptions.IdNotFundException;
import com.app.persistence.entities.groups.FileEntity;
import com.app.persistence.entities.groups.TaskEntity;
import com.app.persistence.entities.users.UserEntity;
import com.app.persistence.repositories.FileRepository;
import com.app.persistence.repositories.TaskRepository;
import com.app.persistence.repositories.UserRepository;
import com.app.services.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements IFileService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ConverterDTO taskMapper;

    @Override
    public FileDTO uploadFile(Long taskId, Long userId, MultipartFile file) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IdNotFundException(taskId));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IdNotFundException(userId));

        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        String fileStorageLocation = "src/main/resources/files/";
        Path path = Paths.get(fileStorageLocation + uniqueFileName);

        try {
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path);

            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(originalFileName);
            fileEntity.setFile_path(path.toString());
            fileEntity.setTask(task);
            fileEntity.setUser(user);

            FileEntity savedFile = fileRepository.save(fileEntity);
            return taskMapper.toFileDTO(savedFile);
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file", e);
        }
    }

    @Override
    public Path getFilePath(Long fileId) {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new IdNotFundException(fileId));
        return Paths.get(fileEntity.getFile_path());
    }
}
