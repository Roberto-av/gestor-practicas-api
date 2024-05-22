package com.app.services;

import com.app.controllers.dto.request.StudentRequestDTO;
import com.app.persistence.entities.students.StudentEntity;
import com.app.persistence.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Optional<StudentEntity> findStudentByControlNumber(int controlNumber) {
        return studentRepository.findStudentByControlNumber(controlNumber);
    }

    public StudentEntity saveStudent(StudentRequestDTO studentDTO) {
        Optional<StudentEntity> existingStudent = studentRepository.findStudentByControlNumber(studentDTO.getControlNumber());

        if (existingStudent.isPresent()) {
            throw new RuntimeException("Student with control number " + studentDTO.getControlNumber() + " already exists");
        } else {
            StudentEntity student = new StudentEntity();
            student.setControlNumber(studentDTO.getControlNumber());
            student.setName(studentDTO.getName());
            student.setEmail(studentDTO.getEmail());
            student.setProgram(studentDTO.getProgram());
            student.setSemester(studentDTO.getSemester());
            student.setShift(studentDTO.getShift());

            return studentRepository.save(student);
        }
    }

    public StudentEntity updateStudent(StudentEntity student) {
        // Verificar si el estudiante ya existe en la base de datos
        Long studentId = student.getId();
        if (studentId == null || !studentRepository.existsById(studentId)) {
            throw new IllegalArgumentException("No se puede actualizar el estudiante porque no existe en la base de datos");
        }
        return studentRepository.save(student);
    }

    public List<StudentEntity> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<StudentEntity> getStudentById(Long id) {
        return studentRepository.findById(id);
    }
}
