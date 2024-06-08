package com.app.controllers;

import com.app.controllers.dto.FileDTO;
import com.app.controllers.dto.SubmissionDTO;
import com.app.controllers.dto.SubmissionFileDTO;
import com.app.controllers.dto.TaskDTO;
import com.app.services.IFileService;
import com.app.services.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private ITaskService taskService;

    @Autowired
    private IFileService fileService;

    @PostMapping("create")
    public ResponseEntity<TaskDTO> createTask(
            @RequestPart("task") TaskDTO taskDTO,
            @RequestPart("files") List<MultipartFile> files) {
        TaskDTO createdTask = taskService.createTask(taskDTO, files);
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        TaskDTO taskDTO = taskService.getTaskById(id);
        return ResponseEntity.ok(taskDTO);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/update")
    public ResponseEntity<TaskDTO> updateTask(@RequestBody TaskDTO taskDTO) {
        Long id = taskDTO.getId();
        TaskDTO updatedTask = taskService.updateTask(id, taskDTO);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/files")
    public ResponseEntity<FileDTO> uploadFile(@PathVariable Long taskId, @RequestParam Long userId, @RequestParam MultipartFile file) {
        FileDTO uploadedFile = taskService.uploadFile(taskId, userId, file);
        return ResponseEntity.ok(uploadedFile);
    }

    @PostMapping("/{taskId}/submit")
    public ResponseEntity<SubmissionDTO> submitTask(@PathVariable Long taskId, @RequestParam Long userId, @RequestParam List<MultipartFile> files) {
        SubmissionDTO submissionDTO = taskService.submitTask(taskId, userId, files);
        return ResponseEntity.ok(submissionDTO);
    }

    @GetMapping("/submissions/{submissionId}/files")
    public ResponseEntity<List<SubmissionFileDTO>> getSubmissionFiles(@PathVariable Long submissionId) {
        List<SubmissionFileDTO> files = taskService.getSubmissionFiles(submissionId);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/{taskId}/submission")
    public ResponseEntity<List<SubmissionDTO>> getSubmissionByTaskAndUser(@PathVariable Long taskId, @RequestParam Long userId) {
        List<SubmissionDTO> submissions = taskService.getSubmissionByTaskAndUser(taskId, userId);
        return ResponseEntity.ok(submissions);
    }

    @PutMapping("/{taskId}/submission/{submissionId}")
    public ResponseEntity<SubmissionDTO> updateSubmission(
            @PathVariable Long taskId,
            @PathVariable Long submissionId,
            @RequestParam Long userId,
            @RequestParam List<MultipartFile> files
    ) {
        SubmissionDTO updatedSubmission = taskService.updateSubmission(taskId, submissionId, userId, files);
        return ResponseEntity.ok(updatedSubmission);
    }

    @DeleteMapping("/delete/submission/{submissionId}/files/{fileId}")
    public ResponseEntity<Void> deleteSubmissionFile(@PathVariable Long submissionId, @PathVariable Long fileId) {
        taskService.deleteSubmissionFile(submissionId, fileId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{taskId}/files")
    public ResponseEntity<TaskDTO> getTaskWithFiles(@PathVariable Long taskId) {
        TaskDTO taskDTO = taskService.getTaskWithFiles(taskId);
        return ResponseEntity.ok(taskDTO);
    }

    @GetMapping("/{taskId}/all-files")
    public ResponseEntity<List<TaskDTO>> getAllTasksWithFiles() {
        List<TaskDTO> tasks = taskService.getAllTasksWithFiles();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/files/{fileId}/path")
    public ResponseEntity<Path> getFilePath(@PathVariable Long fileId) {
        Path filePath = fileService.getFilePath(fileId);
        return ResponseEntity.ok(filePath);
    }
}
