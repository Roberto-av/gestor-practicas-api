package com.app;

import com.app.persistence.entities.institutions.*;
import com.app.persistence.entities.students.ShiftEnum;
import com.app.persistence.entities.students.StudentEntity;
import com.app.persistence.entities.teachers.TeacherEntity;
import com.app.persistence.entities.users.PermissionEntity;
import com.app.persistence.entities.users.RoleEntity;
import com.app.persistence.entities.users.RoleEnum;
import com.app.persistence.entities.users.UserEntity;
import com.app.persistence.repositories.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class GestorPracticasApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestorPracticasApiApplication.class, args);
	}

}
