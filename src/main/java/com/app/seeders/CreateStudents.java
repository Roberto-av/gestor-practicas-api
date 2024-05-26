package com.app.seeders;

import com.app.persistence.entities.students.ShiftEnum;
import com.app.persistence.entities.students.StudentEntity;
import com.app.persistence.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateStudents {

    private final StudentRepository studentRepository;

    @Autowired
    public CreateStudents(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void createAllStudents() {

        StudentEntity student1 = StudentEntity.builder()
                .controlNumber(2020390)
                .name("Roberto Aviles")
                .email("titorey152@gmail.com")
                .program("IDS")
                .semester("6")
                .shift(ShiftEnum.TV)
                .build();

        studentRepository.saveAll(List.of(student1));
    }
}
