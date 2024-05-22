package com.app.persistence.repositories;

import com.app.persistence.entities.students.StudentEntity;
import com.app.persistence.entities.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findStudentByControlNumber(int cont_num);
}
