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
                    requests.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();

                    /* STUDENTS */
                    requests.requestMatchers(HttpMethod.POST, "/api/students/**").hasAnyRole("TEACHER", "ADMIN", "DEVELOPER");
                    requests.requestMatchers(HttpMethod.GET, "/api/students/**").hasAnyRole("TEACHER", "ADMIN", "DEVELOPER");

                    /* MAIL */
                    requests.requestMatchers(HttpMethod.POST, "/api/mail/**").hasAnyRole("TEACHER", "ADMIN", "DEVELOPER");
                    requests.requestMatchers(HttpMethod.POST, "/api/mail/**").hasAnyAuthority("MOD_MAIL_SEND");

                    /* TEACHERS */
                    requests.requestMatchers(HttpMethod.POST, "/api/teachers/create").hasAnyRole("TEACHER", "ADMIN", "DEVELOPER");
                    requests.requestMatchers(HttpMethod.GET, "/api/teachers/**").hasAnyRole("TEACHER", "ADMIN", "DEVELOPER");

                    /* ADDRESSES */
                    requests.requestMatchers(HttpMethod.POST, "/api/addresses/create").hasAnyRole("STUDENT", "ADMIN", "DEVELOPER", "TEACHER");
                    requests.requestMatchers(HttpMethod.GET, "/api/addresses/**").hasAnyRole("STUDENT", "ADMIN", "DEVELOPER", "TEACHER","INVITED");
                    requests.requestMatchers(HttpMethod.PUT, "/api/addresses/update").hasAnyRole("STUDENT", "ADMIN", "DEVELOPER", "TEACHER");

                    /* RESPONSIBLE */
                    requests.requestMatchers(HttpMethod.POST, "/api/responsible/create").hasAnyRole("STUDENT", "ADMIN", "DEVELOPER", "TEACHER");
                    requests.requestMatchers(HttpMethod.GET, "/api/responsible/**").hasAnyRole("STUDENT", "ADMIN", "DEVELOPER", "TEACHER","INVITED");
                    requests.requestMatchers(HttpMethod.PUT, "/api/responsible/update").hasAnyRole("STUDENT", "ADMIN", "DEVELOPER", "TEACHER");

                    /* INSTITUTIONS */
                    requests.requestMatchers(HttpMethod.POST, "/api/institutions/create").hasAnyRole("STUDENT", "ADMIN", "DEVELOPER", "TEACHER");
                    requests.requestMatchers(HttpMethod.GET, "/api/institutions/**").hasAnyRole("STUDENT", "ADMIN", "DEVELOPER", "TEACHER","INVITED");
                    requests.requestMatchers(HttpMethod.PUT, "/api/institutions/update").hasAnyRole("STUDENT", "ADMIN", "DEVELOPER", "TEACHER");

                    /* PROJECTS */
                    requests.requestMatchers(HttpMethod.POST, "/api/projects/create").hasAnyRole("STUDENT", "ADMIN", "DEVELOPER", "TEACHER");
                    requests.requestMatchers(HttpMethod.GET, "/api/projects/**").hasAnyRole("STUDENT", "ADMIN", "DEVELOPER", "TEACHER","INVITED");
                    requests.requestMatchers(HttpMethod.PUT, "/api/projects/update").hasAnyRole("STUDENT", "ADMIN", "DEVELOPER", "TEACHER");

                    /* GROUPS */
                    requests.requestMatchers(HttpMethod.POST, "/api/groups/create").hasAnyRole("STUDENT", "ADMIN", "DEVELOPER", "TEACHER");
                    requests.requestMatchers(HttpMethod.GET, "/api/groups/**").hasAnyRole("STUDENT", "ADMIN", "DEVELOPER", "TEACHER");
                    requests.requestMatchers(HttpMethod.PUT, "/api/groups/update").hasAnyRole("STUDENT", "ADMIN", "DEVELOPER", "TEACHER");

                    /* TASKS */
                    requests.requestMatchers(HttpMethod.POST, "/api/tasks/create").hasAnyRole("ADMIN", "DEVELOPER", "TEACHER");
                    requests.requestMatchers(HttpMethod.POST, "/api/tasks/{id}/upload-file").hasAnyRole("STUDENT", "ADMIN", "DEVELOPER", "TEACHER");
                    requests.requestMatchers(HttpMethod.GET, "/api/tasks/**").hasAnyRole("STUDENT", "ADMIN", "DEVELOPER", "TEACHER");
                    requests.requestMatchers(HttpMethod.PUT, "/api/tasks/update").hasAnyRole("ADMIN", "DEVELOPER", "TEACHER");


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
