package com.example.final_project.config;

import com.example.final_project.jwt.CustomAuthenticationEntryPoint;
import com.example.final_project.jwt.JwtAuthEntrypoint;
import com.example.final_project.jwt.JwtAuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
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
                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**", "/api/attendees/create",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api/landing-page/**",
                                "/api/file/**"
                        ).permitAll()

                        // member controller
                        .requestMatchers(HttpMethod.GET, "/api/members").hasAnyRole( "ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/members/{memberId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/members/{memberId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/members/search").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")

                        // category controller
                        .requestMatchers(HttpMethod.GET, "/api/categories").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/categories").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/{categoryId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/{categoryId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/categories/{cateName}").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")

                        // event controller
                        .requestMatchers(HttpMethod.GET, "/api/events").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/events/{eventId}").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/events").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/events/search").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/events/{eventId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/events/{eventId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/events/active/{eventId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/events/registration-form/{eventId}").hasAnyRole("ADMIN", "SUB_ADMIN")

                        // dashboard controller
                        .requestMatchers(HttpMethod.GET, "/api/dashboards").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")

                        // user request controller
                        .requestMatchers(HttpMethod.GET, "/api/user-requests").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/user-requests/approve/{memberId}").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/user-requests/reject/{memberId}").hasAnyRole("ADMIN")

                        // asset controller
                        .requestMatchers(HttpMethod.GET, "/api/assets").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/assets/search").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/assets/{assetId}").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/assets/update/{assetId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/assets/create").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/assets/delete/{assetId}").hasAnyRole("ADMIN", "SUB_ADMIN")

                        // agenda controller
                        .requestMatchers(HttpMethod.POST, "/api/agendas/{eventId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/agendas/{agendaId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/agendas/{agendaId}").hasAnyRole("ADMIN", "SUB_ADMIN")

                        // attendee controller
                        .requestMatchers(HttpMethod.GET, "/api/attendees/{eventId}").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/attendees/{attendeeId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/attendees/search").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")

                        // profile controller
                        .requestMatchers(HttpMethod.GET, "/api/profiles").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/profiles/update-member/{memberId}").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/profiles/organization").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/profiles/update-organization/{orgId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/profiles/change-password").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")

                        // material controller
                        .requestMatchers(HttpMethod.GET, "/api/materials/getAll/{eventId}").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/materials/count-status/{eventId}").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/materials/search").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/materials/status/{materialId}").hasAnyRole("ADMIN", "SUB_ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/materials/delete/{materialId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/materials/deletes").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/materials/create").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/materials/{materialId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/materials/update/{materialId}").hasAnyRole("ADMIN", "SUB_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/materials/supporters/{materialId}").hasAnyRole("ADMIN", "SUB_ADMIN")

                        .anyRequest().authenticated())
                    .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthEntrypoint))
                    .exceptionHandling(e->e.accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                    )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }}
