package com.jobportal.application.repository;

import com.jobportal.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByJobListingId(Long jobListingId);
}
