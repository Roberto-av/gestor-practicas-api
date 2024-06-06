package com.app.controllers;

import com.app.controllers.dto.AddressDTO;
import com.app.controllers.dto.FileDTO;
import com.app.controllers.dto.TaskDTO;
import com.app.services.impl.TaskServiceImpl;
import org.springframework.core.io.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskServiceImpl taskService;

    @PostMapping("/create")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        TaskDTO task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/with-files")
    public ResponseEntity<List<TaskDTO>> getAllTasksWithFiles() {
        List<TaskDTO> tasks = taskService.getAllTasksWithFiles();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/with-files/{id}")
    public ResponseEntity<TaskDTO> getTaskWithFiles(@PathVariable Long id) {
        TaskDTO task = taskService.getTaskWithFiles(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        try {
            Path filePath = taskService.getFilePath(fileId);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<TaskDTO> updateTask(@Valid @RequestBody TaskDTO taskDTO) {
        Long id = taskDTO.getId();
        TaskDTO updatedTask = taskService.updateTask(id,taskDTO);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{id}/upload-file")
    public ResponseEntity<FileDTO> uploadFile(
            @PathVariable Long id,
            @RequestParam("userId") Long userId,
            @RequestParam("file") MultipartFile file) {
        FileDTO uploadedFile = taskService.uploadFile(id, userId, file);
        return ResponseEntity.ok(uploadedFile);
    }
}
