package com.example.final_project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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

    @Test
    void contextLoads() {
    }

}
