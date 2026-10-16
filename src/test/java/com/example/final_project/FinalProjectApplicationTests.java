package com.example.final_project;

import com.example.final_project.jwt.JwtService;
import com.example.final_project.repository.AssetRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("db")
class FinalProjectApplicationTests extends PostgresTestBase {

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
