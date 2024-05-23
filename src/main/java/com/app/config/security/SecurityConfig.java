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
                    requests.requestMatchers(HttpMethod.POST, "/api/students/**").hasAnyRole("TEACHER", "ADMIN", "DEVELOPER");
                    requests.requestMatchers(HttpMethod.GET, "/api/students/**").hasAnyRole("TEACHER", "ADMIN", "DEVELOPER");
                    requests.requestMatchers(HttpMethod.POST, "/api/mail/**").hasAnyRole("TEACHER", "ADMIN", "DEVELOPER");
                    requests.requestMatchers(HttpMethod.POST, "/api/mail/**").hasAnyAuthority("MOD_MAIL_SEND");
                    requests.requestMatchers(HttpMethod.POST, "/api/teachers/create").hasAnyRole("TEACHER", "ADMIN", "DEVELOPER");
                    requests.requestMatchers(HttpMethod.GET, "/api/teachers/**").hasAnyRole("TEACHER", "ADMIN", "DEVELOPER");
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
