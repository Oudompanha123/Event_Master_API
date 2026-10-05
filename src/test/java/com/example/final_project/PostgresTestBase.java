package com.example.final_project;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Base class for tests that need a real database.
 *
 * <p>The mappers are full of PostgreSQL-only SQL ({@code ::jsonb}, {@code ILIKE},
 * {@code RETURNING *}), so the tests run against an actual PostgreSQL started by
 * Testcontainers rather than an in-memory stand-in. The schema is created from
 * {@code script/scheme.sql} by {@code application-test.properties}.
 *
 * <p>Tests extending this are tagged {@code db} and need a running Docker daemon.
 */
@SpringBootTest
@ActiveProfiles("test")
public abstract class PostgresTestBase {

    /*
     * Started once per JVM and shared by every subclass. A @Container-managed
     * container would be stopped after each test class, which would leave
     * Spring's cached application context pointing at a dead database.
     */
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    static {
        POSTGRES.start();
    }

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }
}
