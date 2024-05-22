package com.app;

import com.app.persistence.entities.users.PermissionEntity;
import com.app.persistence.entities.users.RoleEntity;
import com.app.persistence.entities.users.RoleEnum;
import com.app.persistence.entities.users.UserEntity;
import com.app.persistence.repositories.PermissionRepository;
import com.app.persistence.repositories.RoleRepository;
import com.app.persistence.repositories.UserRepository;
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

	@Bean
	CommandLineRunner init(PermissionRepository permissionRepository, RoleRepository roleRepository) {
		return args -> {
			/* Create PERMISSIONS */

			/* STUDENTS */
			PermissionEntity StudentCreatePermission = PermissionEntity.builder()
					.name("MOD_STUDENT_CREATE")
					.build();

			PermissionEntity StudentReadPermission = PermissionEntity.builder()
					.name("MOD_STUDENT_READ")
					.build();

			PermissionEntity StudentUpdatePermission = PermissionEntity.builder()
					.name("MOD_STUDENT_UPDATE")
					.build();

			PermissionEntity StudentDeletePermission = PermissionEntity.builder()
					.name("MOD_STUDENT_DELETE")
					.build();


			/* TEACH */
			PermissionEntity TeacherCreatePermission = PermissionEntity.builder()
					.name("MOD_TEACHER_CREATE")
					.build();

			PermissionEntity TeacherReadPermission = PermissionEntity.builder()
					.name("MOD_TEACHER_READ")
					.build();

			/* INSTITUTION */

			PermissionEntity InstitutionCreatePermission = PermissionEntity.builder()
					.name("MOD_INSTITUTION_CREATE")
					.build();

			PermissionEntity InstitutionReadPermission = PermissionEntity.builder()
					.name("MOD_INSTITUTION_READ")
					.build();

			PermissionEntity InstitutionUpdatePermission = PermissionEntity.builder()
					.name("MOD_INSTITUTION_UPDATE")
					.build();

			PermissionEntity InstitutionDeletePermission = PermissionEntity.builder()
					.name("MOD_INSTITUTION_DELETE")
					.build();

//			permissionRepository.saveAll(List.of(StudentCreatePermission,
//					StudentReadPermission,
//					StudentUpdatePermission,
//					StudentDeletePermission,
//					TeacherCreatePermission,
//					TeacherReadPermission,
//					InstitutionCreatePermission,
//					InstitutionReadPermission,
//					InstitutionUpdatePermission,
//					InstitutionDeletePermission));


			/* Create ROLES */
			RoleEntity roleAdmin = RoleEntity.builder()
					.roleEnum(RoleEnum.ADMIN)
					.permissionList(Set.of(StudentCreatePermission,
							StudentReadPermission,
							StudentUpdatePermission,
							StudentDeletePermission,
							TeacherCreatePermission,
							TeacherReadPermission))
					.build();

			RoleEntity roleStudent = RoleEntity.builder()
					.roleEnum(RoleEnum.STUDENT)
					.permissionList(Set.of(InstitutionCreatePermission, InstitutionReadPermission))
					.build();

			RoleEntity roleInvited = RoleEntity.builder()
					.roleEnum(RoleEnum.INVITED)
					.permissionList(Set.of(InstitutionReadPermission))
					.build();

			RoleEntity roleDeveloper = RoleEntity.builder()
					.roleEnum(RoleEnum.DEVELOPER)
					.permissionList(Set.of(StudentCreatePermission,
							StudentReadPermission,
							StudentUpdatePermission,
							StudentDeletePermission,
							TeacherCreatePermission,
							TeacherReadPermission,
							InstitutionCreatePermission,
							InstitutionReadPermission,
							InstitutionUpdatePermission,
							InstitutionDeletePermission))
					.build();

			roleRepository.saveAll(List.of(roleAdmin, roleStudent, roleInvited, roleDeveloper));
		};
	}
}
