package com.example.final_project;

import com.example.final_project.jwt.JwtService;
import com.example.final_project.repository.AssetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Checks that the Spring context wires up (security, JWT, mail, Thymeleaf,
 * MyBatis) against an in-memory H2 database, with no Docker involved.
 *
 * <p>This is deliberately limited to startup: the mappers use {@code ::jsonb},
 * {@code data->>'...'}, {@code ILIKE} and {@code RETURNING *}, none of which H2
 * supports, so anything that actually executes a query belongs in a
 * {@link PostgresTestBase} subclass instead.
 */
@SpringBootTest
@ActiveProfiles("h2")
class ContextLoadsWithoutDockerTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        // Naming beans that only exist once security, JWT and the MyBatis mappers
        // have all wired up states what this test checks, instead of leaving it
        // implicit in "no exception was thrown".
        assertThat(context.getBean(JwtService.class)).isNotNull();
        assertThat(context.getBean(AssetRepository.class)).isNotNull();
    }

}
