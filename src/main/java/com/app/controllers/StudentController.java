package com.app.controllers;

import com.app.controllers.dto.StudentInstitutionDTO;
import com.app.controllers.dto.request.StudentRequestDTO;
import com.app.controllers.dto.StudentDTO;
import com.app.services.impl.StudentServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentServiceImpl studentServiceImpl;

    @Autowired
    public StudentController(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createStudent(@RequestBody StudentDTO studentDTO) {
        try {
            StudentDTO savedStudent = studentServiceImpl.saveStudent(studentDTO);
            return ResponseEntity.ok(savedStudent);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentServiceImpl.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<StudentDTO> updateStudent(@Valid @RequestBody StudentDTO studentDTO) {
        Long id = studentDTO.getId();
        StudentDTO updateStudent = studentServiceImpl.updateStudent(id, studentDTO);
        return new ResponseEntity<>(updateStudent, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        StudentDTO studentDTO = studentServiceImpl.getStudentById(id);
        return new ResponseEntity<>(studentDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentServiceImpl.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/subscribe-institution")
    public ResponseEntity<?> subscribeStudentToInstitution(@RequestBody StudentInstitutionDTO studentInstitutionDTO) {
        try {
            studentServiceImpl.subscribeStudentToInstitution(studentInstitutionDTO);
            return ResponseEntity.ok("Student subscribed to institution successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/unsubscribe-institution")
    public ResponseEntity<?> unsubscribeStudentFromInstitution(@RequestBody StudentInstitutionDTO studentInstitutionDTO) {
        try {
            studentServiceImpl.unsubscribeStudentFromInstitution(studentInstitutionDTO);
            return ResponseEntity.ok("Student unsubscribed from institution successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
