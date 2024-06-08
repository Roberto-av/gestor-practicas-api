package com.app.services;

import com.app.controllers.dto.FileDTO;
import com.app.controllers.dto.SubmissionDTO;
import com.app.controllers.dto.SubmissionFileDTO;
import com.app.controllers.dto.TaskDTO;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface ITaskService {
    TaskDTO createTask(TaskDTO taskDTO, List<MultipartFile> files);
    TaskDTO getTaskById(Long id);
    List<TaskDTO> getAllTasks();
    TaskDTO updateTask(Long id, TaskDTO taskDTO);
    void deleteTask(Long id);
    FileDTO uploadFile(Long taskId, Long userId, MultipartFile file);
    List<TaskDTO> getAllTasksWithFiles();
    TaskDTO getTaskWithFiles(Long taskId);
    Path getFilePath(Long fileId);
    SubmissionDTO submitTask(Long taskId, Long userId, List<MultipartFile> files);
    List<SubmissionFileDTO> getSubmissionFiles(Long submissionId);
    SubmissionDTO updateSubmission(Long taskId, Long submissionId, Long userId, List<MultipartFile> files);
    List<SubmissionDTO> getSubmissionByTaskAndUser(Long taskId, Long userId);
    void deleteSubmissionFile(Long submissionId, Long fileId);
}
