package com.app.seeders;

import com.app.persistence.entities.students.ShiftEnum;
import com.app.persistence.entities.students.StudentEntity;
import com.app.persistence.entities.teachers.TeacherEntity;
import com.app.persistence.entities.users.PermissionEntity;
import com.app.persistence.entities.users.RoleEntity;
import com.app.persistence.entities.users.RoleEnum;
import com.app.persistence.entities.users.UserEntity;
import com.app.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class CreateUsers {

    private final UserRepository userRepository;

    @Autowired
    public CreateUsers(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUsers() {
        /* PERMISSIONS */

        /* PERMISSIONS-STUDENTS */
        PermissionEntity studentCreatePermission = PermissionEntity.builder().name("MOD_STUDENT_CREATE").build();
        PermissionEntity studentReadPermission = PermissionEntity.builder().name("MOD_STUDENT_READ").build();
        PermissionEntity studentUpdatePermission = PermissionEntity.builder().name("MOD_STUDENT_UPDATE").build();

        /* PERMISSIONS-TEACHERS */
        PermissionEntity teacherCreatePermission = PermissionEntity.builder().name("MOD_TEACHER_CREATE").build();
        PermissionEntity teacherReadPermission = PermissionEntity.builder().name("MOD_TEACHER_READ").build();

        /* PERMISSIONS-INSTITUTIONS */
        PermissionEntity institutionCreatePermission = PermissionEntity.builder().name("MOD_INSTITUTION_CREATE").build();
        PermissionEntity institutionReadPermission = PermissionEntity.builder().name("MOD_INSTITUTION_READ").build();
        PermissionEntity institutionUpdatePermission = PermissionEntity.builder().name("MOD_INSTITUTION_UPDATE").build();
        PermissionEntity institutionDeletePermission = PermissionEntity.builder().name("MOD_INSTITUTION_DELETE").build();

        /* PERMISSIONS-EMAIL-SENDING */
        PermissionEntity mailSendPermission = PermissionEntity.builder().name("MOD_MAIL_SEND").build();

        /* PERMISSIONS-PROJECTS */
        PermissionEntity projectCreatePermission = PermissionEntity.builder().name("MOD_PROJECT_CREATE").build();
        PermissionEntity projectReadPermission = PermissionEntity.builder().name("MOD_PROJECT_READ").build();
        PermissionEntity projectUpdatePermission = PermissionEntity.builder().name("MOD_PROJECT_UPDATE").build();
        PermissionEntity projectDeletePermission = PermissionEntity.builder().name("MOD_PROJECT_DELETE").build();

        /* PERMISSIONS-GROUPS */
        PermissionEntity groupCreatePermission = PermissionEntity.builder().name("MOD_GROUP_CREATE").build();
        PermissionEntity groupReadPermission = PermissionEntity.builder().name("MOD_GROUP_READ").build();
        PermissionEntity groupUpdatePermission = PermissionEntity.builder().name("MOD_GROUP_UPDATE").build();
        PermissionEntity groupDeletePermission = PermissionEntity.builder().name("MOD_GROUP_DELETE").build();

        /* PERMISSIONS-GROUPS-TASKS */
        PermissionEntity tasksCreatePermission = PermissionEntity.builder().name("MOD_TASK_CREATE").build();
        PermissionEntity tasksReadPermission = PermissionEntity.builder().name("MOD_TASK_READ").build();
        PermissionEntity tasksUpdatePermission = PermissionEntity.builder().name("MOD_TASK_UPDATE").build();
        PermissionEntity tasksDeletePermission = PermissionEntity.builder().name("MOD_TASK_DELETE").build();
        PermissionEntity tasksUploadFilePermission = PermissionEntity.builder().name("MOD_TASK_UPLOAD_FILE").build();

        /* ROLES */
        RoleEntity roleAdmin = RoleEntity.builder()
                .roleEnum(RoleEnum.ADMIN)
                .permissionList(Set.of(studentCreatePermission,
                        studentReadPermission,
                        studentUpdatePermission,
                        teacherCreatePermission,
                        teacherReadPermission,
                        mailSendPermission,
                        institutionReadPermission,
                        institutionCreatePermission))
                .build();

        RoleEntity roleTeacher = RoleEntity.builder()
                .roleEnum(RoleEnum.TEACHER)
                .permissionList(Set.of(studentCreatePermission,
                        studentReadPermission,
                        studentUpdatePermission,
                        teacherCreatePermission,
                        teacherReadPermission,
                        mailSendPermission,
                        institutionCreatePermission,
                        institutionReadPermission,
                        institutionUpdatePermission,
                        institutionDeletePermission,
                        projectCreatePermission,
                        projectReadPermission,
                        projectUpdatePermission,
                        projectDeletePermission,
                        groupCreatePermission,
                        groupReadPermission,
                        groupUpdatePermission,
                        groupDeletePermission,
                        tasksCreatePermission,
                        tasksReadPermission,
                        tasksUpdatePermission,
                        tasksDeletePermission,
                        tasksUploadFilePermission))
                .build();

        RoleEntity roleStudent = RoleEntity.builder()
                .roleEnum(RoleEnum.STUDENT)
                .permissionList(Set.of(institutionCreatePermission,
                        institutionReadPermission,
                        projectCreatePermission,
                        projectReadPermission,
                        groupReadPermission,
                        tasksReadPermission,
                        tasksUploadFilePermission))
                .build();

        RoleEntity roleInvited = RoleEntity.builder()
                .roleEnum(RoleEnum.INVITED)
                .permissionList(Set.of(institutionReadPermission,
                        projectReadPermission))
                .build();

        RoleEntity roleDeveloper = RoleEntity.builder()
                .roleEnum(RoleEnum.DEVELOPER)
                .permissionList(Set.of(studentCreatePermission,
                        studentReadPermission,
                        studentUpdatePermission,
                        teacherCreatePermission,
                        teacherReadPermission,
                        institutionCreatePermission,
                        institutionReadPermission,
                        institutionUpdatePermission,
                        institutionDeletePermission,
                        mailSendPermission,
                        projectCreatePermission,
                        projectReadPermission,
                        projectUpdatePermission,
                        projectDeletePermission,
                        groupCreatePermission,
                        groupReadPermission,
                        groupUpdatePermission,
                        groupDeletePermission,
                        tasksCreatePermission,
                        tasksReadPermission,
                        tasksUpdatePermission,
                        tasksDeletePermission,
                        tasksUploadFilePermission))
                .build();

        /* STUDENTS */
        StudentEntity studentJohn = StudentEntity.builder()
					.controlNumber(123456)
					.name("John Smit")
					.email("John@gmail.com")
					.program("IDS")
					.semester("9")
					.shift(ShiftEnum.TM)
					.build();

        /* CREATE TEACHERS */
			TeacherEntity TeacherIsra = TeacherEntity.builder()
					.name("Isra")
					.email("isra@gmailcom")
					.build();

        /* USERS */
        UserEntity userRoberto = UserEntity.builder()
                .username("Roberto")
                .password("$2a$10$cMY29RPYoIHMJSuwRfoD3eQxU1J5Rww4VnNOUOAEPqCBshkNfrEf6")
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .roles(Set.of(roleAdmin))
                .build();

        UserEntity userJohn = UserEntity.builder()
                .username("John")
                .password("$2a$10$cMY29RPYoIHMJSuwRfoD3eQxU1J5Rww4VnNOUOAEPqCBshkNfrEf6")
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .student(studentJohn)
                .roles(Set.of(roleStudent))
                .build();

        UserEntity userIsra = UserEntity.builder()
                .username("isra")
                .password("$2a$10$cMY29RPYoIHMJSuwRfoD3eQxU1J5Rww4VnNOUOAEPqCBshkNfrEf6")
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .teacher(TeacherIsra)
                .roles(Set.of(roleTeacher))
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

        userRepository.saveAll(List.of(userRoberto, userJohn, userIsra, userAndrea, userTito));
    }
}
