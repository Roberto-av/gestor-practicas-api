package com.app.services.impl;

import com.app.controllers.dto.request.TeacherRequestDTO;
import com.app.persistence.entities.teachers.TeacherEntity;
import com.app.persistence.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl {

    @Autowired
    private TeacherRepository teacherRepository;

    public TeacherEntity saveTeacher(TeacherRequestDTO teacherDTO) {
        Optional<TeacherEntity> existingTeacher = teacherRepository.findTeacherByEmail(teacherDTO.email());

        if (existingTeacher.isPresent()) {
            throw new RuntimeException("Teacher with email " + teacherDTO.email() + " already exists");
        } else {
            TeacherEntity teacher = new TeacherEntity();
            teacher.setName(teacherDTO.name());
            teacher.setEmail(teacherDTO.email());
            return teacherRepository.save(teacher);
        }
    }

    public List<TeacherEntity> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Optional<TeacherEntity> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    public Optional<TeacherEntity> getTeacherByEmail(String email) {
        return teacherRepository.findTeacherByEmail(email);
    }
}
