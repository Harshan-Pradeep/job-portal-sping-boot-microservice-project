package com.jobportal.application.service;

import com.jobportal.application.dto.ApplicationDto;
import com.jobportal.application.entity.Application;
import com.jobportal.application.exception.ResourceNotFoundException;
import com.jobportal.application.mapper.ApplicationMapper;
import com.jobportal.application.repository.ApplicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApplicationServiceImpl implements IApplicationService{

    private ApplicationRepository applicationRepository;

    /**
     * @param applicationDto
     */
    @Override
    public void createApplication(ApplicationDto applicationDto) {
        Application application  = ApplicationMapper.mapToApplication(applicationDto, new Application());
        applicationRepository.save(application);
    }

    /**
     * @param applicationId
     * @return
     */
    @Override
    public ApplicationDto getApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(()->new ResourceNotFoundException("Application", "Id", applicationId.toString()));

        ApplicationDto applicationDto = ApplicationMapper.mapToApplicationDto(application, new ApplicationDto());
        return applicationDto;
    }

    /**
     * @param applicationDto
     * @return
     */
    @Override
    public boolean updateApplication(ApplicationDto applicationDto) {
        boolean isUpdated = false;

        Application existingApplication  = applicationRepository.findById(applicationDto.getId())
                .orElseThrow(()->new ResourceNotFoundException("Application", "Id", applicationDto.getId().toString()));

        Application updateApplication = ApplicationMapper.mapToApplication(applicationDto, existingApplication);
        applicationRepository.save(updateApplication);
        isUpdated = true;

        return isUpdated;
    }

    /**
     * @param applicationId
     * @return
     */
    @Override
    public boolean deleteApplication(Long applicationId) {
        boolean isDeleted = false;

        applicationRepository.findById(applicationId)
                .orElseThrow(()->new ResourceNotFoundException("Application", "Id", applicationId.toString()));

        applicationRepository.deleteById(applicationId);
        isDeleted = true;

        return isDeleted;
    }
}
