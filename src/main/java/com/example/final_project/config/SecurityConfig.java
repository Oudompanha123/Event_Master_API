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
public class SecurityConfig{
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthEntrypoint jwtAuthEntrypoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .cors(withDefaults()).csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/auth/login", "/api/auth/admin-register", "/api/auth/user-register",
                                "/api/auth/set-new-password", "/api/auth/verify", "/api/auth/resend", "/api/auth/org/{code}",
                                "/v3/api-docs/**", "/api/attendee",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api/landing-page/**"
                        ).permitAll()
                        // auth controller
                        .requestMatchers(HttpMethod.PUT, "/api/auth/change-password").hasAnyRole( "ADMIN", "SUB_ADMIN", "/USER")

                        // member controller
                        .requestMatchers(HttpMethod.GET, "/api/member").hasAnyRole( "ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/member/{memberId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/member/{memberId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/member/search").hasAnyRole("ADMIN", "SUB_ADMIN")

                        // category controller
                        .requestMatchers(HttpMethod.GET, "/api/category").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/category").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/category/{categoryId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/category/{categoryId}").hasAnyRole("ADMIN", "SUB_ADMIN")

                        // event controller
                        .requestMatchers(HttpMethod.GET, "/api/event").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/event/{eventId}").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/event").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/event/search").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/event/{eventId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/event/{eventId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/event/active/{eventId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/event/registration-form/{eventId}").hasAnyRole("ADMIN", "SUB_ADMIN")

                        // dashboard controller
                        .requestMatchers(HttpMethod.GET, "/api/dashboard").hasAnyRole("ADMIN", "SUB_ADMIN")

                        // user request controller
                        .requestMatchers(HttpMethod.GET, "/api/user-request").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/user-request/approve/{memberId}").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/user-request/reject/{memberId}").hasAnyRole("ADMIN")

                        // asset controller
                        .requestMatchers(HttpMethod.GET, "/api/asset").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/asset/search/{name}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/asset/{assetId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/asset/update/{assetId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/asset/create").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/asset/delete/{assetId}").hasAnyRole("ADMIN", "SUB_ADMIN")

                        // agenda controller
                        .requestMatchers(HttpMethod.POST, "/api/agenda/{eventId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/agenda/{agendaId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/agenda/{agendaId}").hasAnyRole("ADMIN", "SUB_ADMIN")

                        // attendee controller
                        .requestMatchers(HttpMethod.GET, "/api/attendee/{eventId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/attendee/{attendeeId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/attendee/search").hasAnyRole("ADMIN", "SUB_ADMIN")

                        // profile controller
                        .requestMatchers(HttpMethod.GET, "/api/profile/member").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/profile/update-member/{profileId}").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/profile/organization").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/profile/update-organization/{orgId}").hasRole("ADMIN")

                        // material controller
                        .requestMatchers(HttpMethod.GET, "/api/material").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/material/count-status/{eventId}").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/material/status/{materialId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/material/delete/{materialId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/material/deletes").hasAnyRole("ADMIN", "SUB_ADMIN")

                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthEntrypoint))
                .exceptionHandling(e->e.accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }}
