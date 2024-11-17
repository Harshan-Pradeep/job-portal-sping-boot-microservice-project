package com.jobportal.joblistings;

import com.jobportal.joblistings.AuditAwareImpl.AuditAwareImpl;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
		info = @Info(
				title = "Job Listing microservice REST API Documentation",
				description = "Job Listing microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Harshan Pradeep",
						email = "harshan.pradeep@outlook.com"
				)
		)
)
public class JoblistingsApplication {

	public static void main(String[] args) {
		SpringApplication.run(JoblistingsApplication.class, args);
	}

	@Bean
	public AuditAwareImpl auditAwareImpl() {
		return new AuditAwareImpl();
	}
}
