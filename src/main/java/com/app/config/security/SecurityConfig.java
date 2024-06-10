package com.app.config.security;

import com.app.config.security.filter.JwtTokenValidator;
import com.app.services.impl.UserDetailsServiceImpl;
import com.app.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpS)throws Exception{
        return httpS
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests->{
                    requests.requestMatchers(HttpMethod.POST, "/auth/register").hasAnyRole("TEACHER", "DEVELOPER");
                    requests.requestMatchers(HttpMethod.POST, "/auth/register/student").permitAll();
                    requests.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();

                    /* USERS */
                    requests.requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyAuthority("MOD_USER_READ");
                    requests.requestMatchers(HttpMethod.POST, "/api/users/**").hasAnyAuthority("MOD_STUDENT_UPDATE");
                    requests.requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAnyAuthority("MOD_STUDENT_DELETE");

                    /* STUDENTS */
                    requests.requestMatchers(HttpMethod.POST, "/api/students/create").hasAnyAuthority("MOD_STUDENT_CREATE");
                    requests.requestMatchers(HttpMethod.GET, "/api/students/**").hasAnyAuthority("MOD_STUDENT_READ");
                    requests.requestMatchers(HttpMethod.PUT, "/api/students/update").hasAnyAuthority("MOD_STUDENT_UPDATE");
                    requests.requestMatchers(HttpMethod.DELETE, "/api/students/{id}").hasAnyAuthority("MOD_STUDENT_DELETE");
                    requests.requestMatchers(HttpMethod.POST, "/api/students/subscribe-institution").hasAnyRole("STUDENT", "TEACHER");
                    requests.requestMatchers(HttpMethod.POST, "/api/students/unsubscribe-institution").hasAnyRole("STUDENT", "TEACHER");

                    /* MAIL */
                    requests.requestMatchers(HttpMethod.POST, "/api/mail/**").hasAnyAuthority("MOD_MAIL_SEND");

                    /* TEACHERS */
                    requests.requestMatchers(HttpMethod.POST, "/api/teachers/create").hasAnyAuthority("MOD_TEACHER_CREATE");
                    requests.requestMatchers(HttpMethod.GET, "/api/teachers/**").hasAnyAuthority("MOD_TEACHER_READ");

//                    /* ADDRESSES */
//                    requests.requestMatchers(HttpMethod.POST, "/api/addresses/create").hasAnyAuthority("MOD_STUDENT_CREATE");
//                    requests.requestMatchers(HttpMethod.GET, "/api/addresses/**").hasAnyAuthority("MOD_STUDENT_CREATE");
//                    requests.requestMatchers(HttpMethod.PUT, "/api/addresses/update").hasAnyAuthority("MOD_STUDENT_CREATE");
//
//                    /* RESPONSIBLE */
//                    requests.requestMatchers(HttpMethod.POST, "/api/responsible/create").hasAnyAuthority("STUDENT", "ADMIN", "DEVELOPER", "TEACHER");
//                    requests.requestMatchers(HttpMethod.GET, "/api/responsible/**").hasAnyAuthority("STUDENT", "ADMIN", "DEVELOPER", "TEACHER","INVITED");
//                    requests.requestMatchers(HttpMethod.PUT, "/api/responsible/update").hasAnyAuthority("STUDENT", "ADMIN", "DEVELOPER", "TEACHER");

                    /* INSTITUTIONS */
                    requests.requestMatchers(HttpMethod.POST, "/api/institutions/create").hasAnyAuthority("MOD_INSTITUTION_CREATE");
                    requests.requestMatchers(HttpMethod.GET, "/api/institutions/**").hasAnyAuthority("MOD_INSTITUTION_READ");
                    requests.requestMatchers(HttpMethod.PUT, "/api/institutions/update").hasAnyAuthority("MOD_INSTITUTION_UPDATE");
                    requests.requestMatchers(HttpMethod.DELETE, "/api/institutions/delete/{id}").hasAnyAuthority("MOD_INSTITUTION_DELETE");


                    /* PROJECTS */
                    requests.requestMatchers(HttpMethod.POST, "/api/projects/create").hasAnyAuthority("MOD_PROJECT_CREATE");
                    requests.requestMatchers(HttpMethod.GET, "/api/projects/**").hasAnyAuthority("MOD_PROJECT_READ");
                    requests.requestMatchers(HttpMethod.PUT, "/api/projects/update").hasAnyAuthority("MOD_PROJECT_UPDATE");

                    /* GROUPS */
                    requests.requestMatchers(HttpMethod.POST, "/api/groups/create").hasAnyAuthority("MOD_GROUP_CREATE");
                    requests.requestMatchers(HttpMethod.GET, "/api/groups/**").hasAnyAuthority("MOD_GROUP_READ");
                    requests.requestMatchers(HttpMethod.PUT, "/api/groups/update").hasAnyAuthority("MOD_GROUP_UPDATE");
                    requests.requestMatchers(HttpMethod.DELETE, "/api/groups/delete/{id}").hasAnyAuthority("MOD_GROUP_DELETE");


                    /* TASKS */
                    requests.requestMatchers(HttpMethod.POST, "/api/tasks/create").hasAnyAuthority("MOD_TASK_CREATE");
                    requests.requestMatchers(HttpMethod.POST, "/api/tasks/create-with-files").hasAnyAuthority("MOD_TASK_CREATE");
                    requests.requestMatchers(HttpMethod.POST, "/api/tasks/{id}/upload-file").hasAnyAuthority("MOD_TASK_UPLOAD_FILE");
                    requests.requestMatchers(HttpMethod.GET, "/api/tasks/**").hasAnyAuthority("MOD_TASK_READ");
                    requests.requestMatchers(HttpMethod.PUT, "/api/tasks/update").hasAnyAuthority("MOD_TASK_UPDATE");
                    requests.requestMatchers(HttpMethod.DELETE, "/api/tasks/delete/{id}").hasAnyAuthority("MOD_TASK_DELETE");
                    requests.requestMatchers(HttpMethod.POST, "/api/tasks/**").hasAnyRole("TEACHER","STUDENT");
                    requests.requestMatchers(HttpMethod.PUT, "/api/tasks/{taskId}/submission/{submissionId}").hasAnyRole("TEACHER","STUDENT");
                    requests.requestMatchers(HttpMethod.DELETE, "/api/tasks/delete/submission/{submissionId}/files/{fileId}").hasAnyRole("TEACHER","STUDENT");

                    requests.anyRequest().denyAll();
                })
                .cors(cors->{
                })
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsServiceImp){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsServiceImp);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
}
