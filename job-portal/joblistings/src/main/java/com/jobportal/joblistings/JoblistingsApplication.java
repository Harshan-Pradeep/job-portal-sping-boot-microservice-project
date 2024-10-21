package com.jobportal.joblistings;

import com.jobportal.joblistings.AuditAwareImpl.AuditAwareImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class JoblistingsApplication {

	public static void main(String[] args) {
		SpringApplication.run(JoblistingsApplication.class, args);
	}

	@Bean
	public AuditAwareImpl auditAwareImpl() {
		return new AuditAwareImpl();
	}
}
