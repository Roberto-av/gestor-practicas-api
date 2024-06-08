package com.app.services.impl;

import com.app.components.ConverterDTO;
import com.app.controllers.dto.FileDTO;
import com.app.controllers.dto.SubmissionDTO;
import com.app.controllers.dto.SubmissionFileDTO;
import com.app.controllers.dto.TaskDTO;
import com.app.exceptions.IdNotFundException;
import com.app.exceptions.UnauthorizedException;
import com.app.persistence.entities.groups.*;
import com.app.persistence.entities.users.UserEntity;
import com.app.persistence.repositories.*;
import com.app.services.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements ITaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private SubmissionFileRepository SubmissionFileRepository;

    @Autowired
    private ConverterDTO taskMapper;

    @Override
    public TaskDTO createTask(TaskDTO taskDTO, List<MultipartFile> files) {
        UserEntity user = userRepository.findById(taskDTO.getUser().getId())
                .orElseThrow(() -> new IdNotFundException(taskDTO.getUser().getId()));
        GroupEntity group = groupRepository.findById(taskDTO.getGroup().getId())
                .orElseThrow(() -> new IdNotFundException(taskDTO.getGroup().getId()));
        TaskEntity taskEntity = taskMapper.toEntity(taskDTO, user, group);
        TaskEntity savedTask = taskRepository.save(taskEntity);

        for (MultipartFile file : files) {
            uploadFile(savedTask.getId(), taskDTO.getUser().getId(), file);
        }

        return taskMapper.toDTO(savedTask);
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new IdNotFundException(id));
        return taskMapper.toDTO(taskEntity);
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        List<TaskEntity> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        TaskEntity existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new IdNotFundException(id));

        existingTask.setTittle(taskDTO.getTittle());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setInitialDate(taskDTO.getInitialDate());
        existingTask.setEndDate(taskDTO.getEndDate());

        UserEntity user = userRepository.findById(taskDTO.getUser().getId())
                .orElseThrow(() -> new IdNotFundException(taskDTO.getUser().getId()));
        existingTask.setUser(user);

        GroupEntity group = groupRepository.findById(taskDTO.getGroup().getId())
                .orElseThrow(() -> new IdNotFundException(taskDTO.getGroup().getId()));
        existingTask.setGroup(group);

        existingTask.setStatusTask(taskDTO.getStatusTask());

        TaskEntity savedTask = taskRepository.save(existingTask);
        return taskMapper.toDTO(savedTask);
    }

    @Override
    public void deleteTask(Long id) {
        TaskEntity existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new IdNotFundException(id));
        taskRepository.delete(existingTask);
    }

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
    public List<TaskDTO> getAllTasksWithFiles() {
        List<TaskEntity> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(task -> {
                    TaskDTO taskDTO = taskMapper.toDTO(task);
                    Optional<FileEntity> files = fileRepository.findById(task.getId());
                    taskDTO.setFiles(files.stream().map(taskMapper::toFileDTO).toList());
                    return taskDTO;
                })
                .toList();
    }

    @Override
    public TaskDTO getTaskWithFiles(Long taskId) {
        TaskEntity taskEntity = taskRepository.findById(taskId)
                .orElseThrow(() -> new IdNotFundException(taskId));
        TaskDTO taskDTO = taskMapper.toDTO(taskEntity);
        Optional<FileEntity> files = fileRepository.findById(taskId);
        taskDTO.setFiles(files.stream().map(taskMapper::toFileDTO).toList());
        return taskDTO;
    }

    @Override
    public Path getFilePath(Long fileId) {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new IdNotFundException(fileId));
        return Paths.get(fileEntity.getFile_path());
    }

    @Override
    public SubmissionDTO submitTask(Long taskId, Long userId, List<MultipartFile> files) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IdNotFundException(taskId));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IdNotFundException(userId));

        SubmissionEntity submissionEntity = SubmissionEntity.builder()
                .task(task)
                .user(user)
                .build();

        SubmissionEntity savedSubmission = submissionRepository.save(submissionEntity);

        for (MultipartFile file : files) {
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            String fileStorageLocation = "src/main/resources/files/submissions/";
            Path path = Paths.get(fileStorageLocation + uniqueFileName);

            try {
                Files.createDirectories(path.getParent());
                Files.copy(file.getInputStream(), path);

                SubmissionFileEntity submissionFileEntity = new SubmissionFileEntity();
                submissionFileEntity.setName(originalFileName);
                submissionFileEntity.setFilePath(path.toString());
                submissionFileEntity.setSubmission(savedSubmission);

                SubmissionFileRepository.save(submissionFileEntity);
            } catch (IOException e) {
                throw new RuntimeException("Error uploading file", e);
            }
        }

        return taskMapper.toSubmissionDTO(savedSubmission);
    }

    @Override
    public List<SubmissionFileDTO> getSubmissionFiles(Long submissionId) {
        SubmissionEntity submissionEntity = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IdNotFundException(submissionId));

        List<SubmissionFileEntity> submissionFiles = submissionEntity.getFiles();
        if (submissionFiles == null || submissionFiles.isEmpty()) {
            return Collections.emptyList(); // Retorna una lista vacía si no hay archivos asociados
        }

        return submissionFiles.stream()
                .map(taskMapper::toSubmissionFileDTO) // Convierte las entidades de archivos de presentación en objetos SubmissionFileDTO
                .collect(Collectors.toList());
    }

    @Override
    public SubmissionDTO updateSubmission(Long taskId, Long submissionId, Long userId, List<MultipartFile> files) {
        // Verificar si la tarea y la entrega existen
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IdNotFundException(taskId));

        SubmissionEntity submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IdNotFundException(submissionId));

        // Verificar si el usuario es el propietario de la entrega
        if (!submission.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("User is not authorized to update this submission");
        }

        // Actualizar los archivos adjuntos de la entrega
        List<SubmissionFileEntity> existingFiles = submission.getFiles();
        for (MultipartFile file : files) {
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            String fileStorageLocation = "src/main/resources/files/submissions/";
            Path path = Paths.get(fileStorageLocation + uniqueFileName);

            try {
                Files.createDirectories(path.getParent());
                Files.copy(file.getInputStream(), path);

                SubmissionFileEntity submissionFileEntity = new SubmissionFileEntity();
                submissionFileEntity.setName(originalFileName);
                submissionFileEntity.setFilePath(path.toString());
                submissionFileEntity.setSubmission(submission);

                // Si ya existe un archivo con el mismo nombre, actualizarlo en lugar de crear uno nuevo
                Optional<SubmissionFileEntity> existingFile = existingFiles.stream()
                        .filter(f -> f.getName().equals(originalFileName))
                        .findFirst();
                if (existingFile.isPresent()) {
                    SubmissionFileEntity updatedFile = existingFile.get();
                    updatedFile.setFilePath(path.toString());
                    SubmissionFileRepository.save(updatedFile);
                } else {
                    SubmissionFileRepository.save(submissionFileEntity);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error uploading file", e);
            }
        }

        return taskMapper.toSubmissionDTO(submission);
    }

    @Override
    public List<SubmissionDTO> getSubmissionByTaskAndUser(Long taskId, Long userId) {
        // Obtener la tarea por su ID
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IdNotFundException(taskId));

        // Obtener el usuario por su ID
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IdNotFundException(userId));

        // Obtener todas las presentaciones asociadas a la tarea y el usuario
        List<SubmissionEntity> submissions = submissionRepository.findByTaskAndUser(task, user);

        // Convertir las entidades de presentaciones en objetos DTO
        return submissions.stream()
                .map(taskMapper::toSubmissionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSubmissionFile(Long submissionId, Long fileId) {
        // Verificar si la entrega existe
        SubmissionEntity submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IdNotFundException(submissionId));

        // Verificar si el archivo existe
        SubmissionFileEntity file = SubmissionFileRepository.findById(fileId)
                .orElseThrow(() -> new IdNotFundException(fileId));

        // Verificar si el archivo pertenece a la entrega
        if (!file.getSubmission().getId().equals(submissionId)) {
            throw new UnauthorizedException("File does not belong to this submission");
        }

        // Eliminar el archivo del sistema de archivos
        Path path = Paths.get(file.getFilePath());
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting file", e);
        }

        // Eliminar el archivo de la base de datos
        SubmissionFileRepository.delete(file);
    }

}