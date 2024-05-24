package com.app.controllers;

import com.app.controllers.dto.AddressDTO;
import com.app.controllers.dto.FileDTO;
import com.app.controllers.dto.TaskDTO;
import com.app.services.impl.TaskServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PutMapping("/update")
    public ResponseEntity<TaskDTO> updateTask(@Valid @RequestBody TaskDTO taskDTO) {
        Long id = taskDTO.getId();
        TaskDTO updatedTask = taskService.updateTask(id,taskDTO);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/upload-file")
    public ResponseEntity<FileDTO> uploadFile(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        FileDTO uploadedFile = taskService.uploadFile(id, file);
        return ResponseEntity.ok(uploadedFile);
    }
}
