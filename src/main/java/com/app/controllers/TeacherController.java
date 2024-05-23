package com.app.controllers;

import com.app.controllers.dto.request.TeacherRequestDTO;
import com.app.persistence.entities.teachers.TeacherEntity;
import com.app.services.impl.TeacherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherServiceImpl teacherService;

    @PostMapping("/create")
    public ResponseEntity<?> createTeacher(@RequestBody TeacherRequestDTO teacherDTO) {
        try {
            TeacherEntity savedTeacher = teacherService.saveTeacher(teacherDTO);
            return ResponseEntity.ok(savedTeacher);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllTeachers() {
        List<TeacherEntity> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeacherById(@PathVariable Long id) {
        Optional<TeacherEntity> teacher = teacherService.getTeacherById(id);
        if (teacher.isPresent()) {
            return ResponseEntity.ok(teacher.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher not found");
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getTeacherByEmail(@PathVariable String email) {
        Optional<TeacherEntity> teacher = teacherService.getTeacherByEmail(email);
        if (teacher.isPresent()) {
            return ResponseEntity.ok(teacher.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher not found");
        }
    }
}
