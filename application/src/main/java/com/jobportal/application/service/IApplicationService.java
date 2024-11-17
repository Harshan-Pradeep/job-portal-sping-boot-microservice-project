package com.jobportal.application.service;

import com.jobportal.application.dto.ApplicationDto;

public interface IApplicationService {

    void createApplication(ApplicationDto applicationDto);

    ApplicationDto getApplication(Long applicationId);

    boolean updateApplication(ApplicationDto applicationDto);

    boolean deleteApplication(Long applicationId);
}
