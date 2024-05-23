package com.app.services.impl;

import com.app.controllers.dto.request.AuthCreateUserRequest;
import com.app.controllers.dto.request.AuthLoginRequest;
import com.app.controllers.dto.response.AuthResponse;
import com.app.persistence.entities.students.StudentEntity;
import com.app.persistence.entities.teachers.TeacherEntity;
import com.app.persistence.entities.users.RoleEntity;
import com.app.persistence.entities.users.UserEntity;
import com.app.persistence.repositories.RoleRepository;
import com.app.persistence.repositories.StudentRepository;
import com.app.persistence.repositories.TeacherRepository;
import com.app.persistence.repositories.UserRepository;
import com.app.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        userEntity.getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_" .concat(role.getRoleEnum().name()))));

        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(),
                authorityList);
    }

    public AuthResponse loginUser(AuthLoginRequest authLoginRequest){

        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication auth = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String accesToken = jwtUtils.createToken(auth);

        return new AuthResponse(username, "User loged successfuly", accesToken, true);
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);

        if(userDetails == null){
            throw new BadCredentialsException("Username or password is incorrect");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public AuthResponse createUser(AuthCreateUserRequest authCreateUserRequest) {
        String username = authCreateUserRequest.username();
        String password = authCreateUserRequest.password();
        List<String> roleRequest = authCreateUserRequest.roleRequest().roles();

        Set<RoleEntity> roleEntities = new HashSet<>(roleRepository.findRoleEntitiesByRoleEnumIn(roleRequest));

        if (roleEntities.isEmpty()) {
            throw new IllegalArgumentException("No role found");
        }

        // Crear el builder
        UserEntity.UserEntityBuilder userEntityBuilder = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(roleEntities)
                .isEnabled(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credentialNoExpired(true);

        // Asociar estudiante si se proporciona el ID
        if (authCreateUserRequest.studentId() != null) {
            StudentEntity student = studentRepository.findById(authCreateUserRequest.studentId())
                    .orElseThrow(() -> new RuntimeException("Student with ID " + authCreateUserRequest.studentId() + " not found"));
            userEntityBuilder.student(student);
        }

        // Asociar profesor si se proporciona el ID
        if (authCreateUserRequest.teacherId() != null) {
            TeacherEntity teacher = teacherRepository.findById(authCreateUserRequest.teacherId())
                    .orElseThrow(() -> new RuntimeException("Teacher with ID " + authCreateUserRequest.teacherId() + " not found"));
            userEntityBuilder.teacher(teacher);
        }

        // Construir la instancia de UserEntity
        UserEntity userEntity = userEntityBuilder.build();

        UserEntity userCreate = userRepository.save(userEntity);

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        userCreate.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name())));

        userCreate.getRoles()
                .stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreate.getUsername(), userCreate.getPassword(), authorityList);

        String accessToken = jwtUtils.createToken(authentication);
        return new AuthResponse(userCreate.getUsername(), "User Created Successfully", accessToken, true);
    }

    public UserEntity createUserWithRole(String email, String username, String password, String role) {
        Optional<StudentEntity> studentOptional = studentRepository.findStudentByEmail(email);
        if (!studentOptional.isPresent()) {
            throw new RuntimeException("Student not found for email: " + email);
        }

        StudentEntity studentEntity = studentOptional.get();

        Set<RoleEntity> roleEntities = new HashSet<>(roleRepository.findRoleEntitiesByRoleEnumIn(Collections.singletonList(role)));
        if (roleEntities.isEmpty()) {
            throw new IllegalArgumentException("No role found");
        }

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(roleEntities)
                .isEnabled(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credentialNoExpired(true)
                .student(studentEntity)
                .build();

        return userRepository.save(userEntity);
    }
}
