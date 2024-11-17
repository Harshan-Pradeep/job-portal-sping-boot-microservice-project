package com.jobportal.application.mapper;

import com.jobportal.application.dto.ApplicationDto;
import com.jobportal.application.entity.Application;

public class ApplicationMapper {
    public static ApplicationDto mapToApplicationDto(Application application, ApplicationDto applicationDto) {
        applicationDto.setId(application.getId());
        applicationDto.setJobListingId(application.getJobListingId());
        applicationDto.setApplicantId(application.getApplicantId());
        applicationDto.setStatus(application.getStatus());
        applicationDto.setResumeUrl(application.getResumeUrl());
        applicationDto.setCoverLetter(application.getCoverLetter());
        return applicationDto;
    }

    public static Application mapToApplication(ApplicationDto applicationDto, Application application) {
        application.setId(applicationDto.getId());
        application.setJobListingId(applicationDto.getJobListingId());
        application.setApplicantId(applicationDto.getApplicantId());
        application.setStatus(applicationDto.getStatus());
        application.setResumeUrl(applicationDto.getResumeUrl());
        application.setCoverLetter(applicationDto.getCoverLetter());
        return application;
    }
}
