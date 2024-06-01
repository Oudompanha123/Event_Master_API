package com.example.final_project.config;

import com.example.final_project.jwt.JwtAuthEntrypoint;
import com.example.final_project.jwt.JwtAuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthEntrypoint jwtAuthEntrypoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http
                .cors(withDefaults()).csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/auth/**","/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api/landing-page/**"
                        ).permitAll()
                        // member controller
                        .requestMatchers(HttpMethod.GET, "/api/member").hasAnyRole( "ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/member/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/member/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/member/search").hasAnyRole("ADMIN", "SUB_ADMIN")

                        // category controller
                        .requestMatchers(HttpMethod.GET, "/api/category").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/category").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/category/{id}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/category/{id}").hasAnyRole("ADMIN", "SUB_ADMIN")

                        // event controller
                        .requestMatchers(HttpMethod.GET, "/api/event").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/event/{id}").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/event").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/event/search").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/event/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/event/{id}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/event/active/{id}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/event/registration-form/{id}").hasAnyRole("ADMIN", "SUB_ADMIN")

                        // dashboard controller
                        .requestMatchers(HttpMethod.GET, "/api/dashboard").hasAnyRole("ADMIN", "SUB_ADMIN")

                        // notifications controller
                        .requestMatchers(HttpMethod.GET, "/api/user-request").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/user-request/approve/{id}").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/user-request/reject/{id}").hasAnyRole("ADMIN")

                        // asset controller
                        .requestMatchers(HttpMethod.GET, "/api/asset").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/asset/search/{name}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/asset/{id}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/asset/update/{id}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/asset/create").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/asset/delete/{id}").hasAnyRole("ADMIN", "SUB_ADMIN")

                        // agenda controller
                        .requestMatchers(HttpMethod.POST, "/api/agenda/{id}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/agenda/{id}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/agenda/{id}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthEntrypoint))
                .exceptionHandling(e->e.accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }}
