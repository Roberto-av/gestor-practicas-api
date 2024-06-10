package com.app.seeders;

import com.app.persistence.entities.students.StudentEntity;
import com.app.persistence.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Component
public class CreateStudents {

    private static final Logger logger = LoggerFactory.getLogger(CreateStudents.class);


    private final StudentRepository studentRepository;
    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public CreateStudents(StudentRepository studentRepository, ObjectMapper jacksonObjectMapper) {
        this.studentRepository = studentRepository;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    public void createAllStudents() {
        try (InputStream inputStream = getClass().getResourceAsStream("/data/json/students.json")) {
            List<StudentEntity> students = jacksonObjectMapper.readValue(inputStream, new TypeReference<>() {
            });
            for (StudentEntity student : students) {
                Optional<StudentEntity> existingStudent = studentRepository.findStudentByEmail(student.getEmail());
                if (existingStudent.isEmpty()) {
                    studentRepository.save(student);
                } else {
                    logger.info("Student with email {} already exists. Skipping...", student.getEmail());
                }
            }
        } catch (Exception e) {
            logger.error("Error reading students.json", e);
        }
    }
}
