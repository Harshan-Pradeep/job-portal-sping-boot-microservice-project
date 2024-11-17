package com.jobportal.application.service;

import com.itextpdf.text.DocumentException;
import com.jobportal.application.dto.ApplicationDto;

public interface IApplicationService {

    void createApplication(ApplicationDto applicationDto);

    ApplicationDto getApplication(Long applicationId);

    boolean updateApplication(ApplicationDto applicationDto);

    boolean deleteApplication(Long applicationId);

    byte[] getAllApplicationsAsPdf(Long jobListingId) throws DocumentException;
}
