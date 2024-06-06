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
            studentRepository.saveAll(students);
        } catch (Exception e) {
            logger.error("Error reading students.json", e);
        }
    }
}
