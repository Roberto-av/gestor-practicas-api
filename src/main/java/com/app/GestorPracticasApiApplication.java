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

	@Bean
	CommandLineRunner init(PermissionRepository permissionRepository,
						   RoleRepository roleRepository,
						   UserRepository userRepository,
						   StudentRepository studentRepository,
						   InstitutionRepository institutionRepository,
						   ProjectRepository projectRepository) {
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

			/* Mail */
			PermissionEntity MailSendPermission = PermissionEntity.builder()
					.name("MOD_MAIL_SEND")
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
							TeacherReadPermission,
							MailSendPermission))
					.build();

			RoleEntity roleTeacher = RoleEntity.builder()
					.roleEnum(RoleEnum.TEACHER)
					.permissionList(Set.of(StudentCreatePermission,
							StudentReadPermission,
							StudentUpdatePermission,
							StudentDeletePermission,
							TeacherCreatePermission,
							TeacherReadPermission,
							MailSendPermission,
							InstitutionCreatePermission,
							InstitutionReadPermission,
							InstitutionUpdatePermission,
							InstitutionDeletePermission))
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
							InstitutionDeletePermission,
							MailSendPermission))
					.build();

			//roleRepository.saveAll(List.of(roleAdmin, roleStudent, roleInvited, roleDeveloper, roleTeacher));

			/* CREATE STUDENTS */
			StudentEntity studentRoberto = StudentEntity.builder()
					.controlNumber(123456)
					.name("Roberto Aviles")
					.email("avilesrobertoe@gmail.com")
					.program("IDS")
					.semester("6")
					.shift(ShiftEnum.TV)
					.build();

			StudentEntity studentMedina = StudentEntity.builder()
					.controlNumber(123450)
					.name("Medina")
					.email("medina@gmail.com")
					.program("IDS")
					.semester("6")
					.shift(ShiftEnum.TV)
					.build();

			StudentEntity studentTito1 = StudentEntity.builder()
					.controlNumber(123451)
					.name("tito")
					.email("titorey152@gmail.com")
					.program("IDS")
					.semester("6")
					.shift(ShiftEnum.TV)
					.build();

			StudentEntity studentJavier = StudentEntity.builder()
					.controlNumber(123452)
					.name("Javier Hernandez")
					.email("javier934hernandez@gmail.com")
					.program("IDS")
					.semester("6")
					.shift(ShiftEnum.TV)
					.build();

			studentRepository.saveAll(List.of(studentMedina, studentTito1, studentJavier));

			/* CREATE TEACHERS */
			TeacherEntity TeacherIsra = TeacherEntity.builder()
					.name("Isra")
					.email("isra@gmailcom")
					.build();

			/* CREATE USERS */
			UserEntity userRoberto = UserEntity.builder()
					.username("Roberto")
					.password("$2a$10$cMY29RPYoIHMJSuwRfoD3eQxU1J5Rww4VnNOUOAEPqCBshkNfrEf6")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleAdmin))
					.build();

			UserEntity userAntonio = UserEntity.builder()
					.username("Antonio")
					.password("$2a$10$cMY29RPYoIHMJSuwRfoD3eQxU1J5Rww4VnNOUOAEPqCBshkNfrEf6")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.student(studentRoberto)
					.roles(Set.of(roleStudent))
					.build();

			UserEntity userIsra = UserEntity.builder()
					.username("isra")
					.password("$2a$10$cMY29RPYoIHMJSuwRfoD3eQxU1J5Rww4VnNOUOAEPqCBshkNfrEf6")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleTeacher))
					.teacher(TeacherIsra)
					.build();

			UserEntity userAndrea = UserEntity.builder()
					.username("andrea")
					.password("$2a$10$cMY29RPYoIHMJSuwRfoD3eQxU1J5Rww4VnNOUOAEPqCBshkNfrEf6")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleInvited))
					.build();

			UserEntity userTito = UserEntity.builder()
					.username("tito")
					.password("$2a$10$cMY29RPYoIHMJSuwRfoD3eQxU1J5Rww4VnNOUOAEPqCBshkNfrEf6")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleDeveloper))
					.build();

			userRepository.saveAll(List.of(userRoberto, userAntonio, userAndrea, userTito, userIsra));


			/* INSTITUTIONS */
			AddressEntity address = AddressEntity.builder()
					.street("JALISCO 87 LOCAL 2")
					.city("LA PAZ")
					.state("BAJA CALIFORNIA SUR")
					.postalCode("23097")
					.country("México")
					.build();

			ResponsibleEntity responsible1 = ResponsibleEntity.builder()
					.name("EMMANUEL PATRICIO VAZQUEZ SANCHEZ")
					.position("PROPIETARIO")
					.education("PASANTE INGENIERIA EN DESARROLLO COMPUTACIONAL")
					.email("epvazquez@smcbcs.mx")
					.confirmEmail("epvazquez@smcbcs.mx")
					.phone("6121416759")
					.build();

//			ResponsibleEntity responsible2 = ResponsibleEntity.builder()
//					.name("Jane Smith")
//					.position("Director")
//					.email("janesmith@example.com")
//					.phone("098-765-4321")
//					.build();

			ProjectEntity proyecto1 = ProjectEntity.builder()
					.name("ESEGUIMIENTO DE ALUMNOS DE BAJO APROVECHAMIENTO ESCOLAR")
					.schedule("09:00-13:00")
					.numHours(20)
					.justification("SE BUSCA DAR SEGUIMIENTO OPORTUNO A AQUELLOS ALUMNOS DE BAJO APROVECHAMIENTO ESCOLAR, MEDIANTE EL DRIVE DE SEGUIMIENTO CON INFORMACION OTORGADA POR MAESTROS DE LAS DIFERENTES ASIGNATURAS.")
					.objectives("CANALIZAR A LOS ALUMNOS QUE PRESENTAN BAJO APROVECHAMIENTO AL DEPARTAMENTO DE ORIENTACION ESCOLAR CON LA INTENCION DE EVITAR LA DESERCION\n" +
							"\n")
					.functions("CAPTURAR LA INFORMACION DE LAS DIFERENTES ASIGNATURAS EN DRIVE DE SEGUIMIENTO")
					.requested(1)
					.registered(1)
					.build();

			//rojectRepository.save(proyecto1);
			InstitutionEntity institution = InstitutionEntity.builder()
					.name("SERVICIOS MULTIPLES EN COMPUTO")
					.rfc("VASE7409115I6")
					.companyName("EMMANUEL PATRICIO VAZQUEZ SANCHEZ")
					.sector(SectorEnum.PRIVATE)
					.giro("VENTA AL POR MENOR DE EQUIPO DE COMPUTO, REDES Y CCTV")
					.web("NO")
					.support(false)
					.address(address)
					.telephoneNumber("6121416759")
					.modality(ModalityEnum.HYBRID)
					.responsible(responsible1)
					.build();

			institutionRepository.save(institution);

		};
	}
}
