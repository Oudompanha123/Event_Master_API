package com.example.final_project;

import com.example.final_project.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@OpenAPIDefinition(info = @Info(title = "EventMaster API",
		version = "v1",
		description = "Watch as your vision comes to life flawlessly. \n" +
				"EventMaster handles all the logistics, coordinating \n" +
				"vendors, managing registrations,  and providing \n" +
				"real-time updates, so you can focus on making \n" +
				"memories."))
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		in = SecuritySchemeIn.HEADER
)
@SpringBootApplication
@EnableScheduling
@AllArgsConstructor
@Slf4j
public class FinalProjectApplication {
	private final EventRepository eventRepository;
	public static void main(String[] args) {
		SpringApplication.run(FinalProjectApplication.class, args);
	}

	@Scheduled(cron = "0 0 * * * *")
	public void function(){
		try {
			eventRepository.autoCloseEvent();
		} catch (DataAccessException e) {
			// The database may be briefly unreachable; the next hourly run will retry.
			log.warn("autoCloseEvent skipped - database unavailable: {}", e.getMostSpecificCause().getMessage());
		}
	}
}
