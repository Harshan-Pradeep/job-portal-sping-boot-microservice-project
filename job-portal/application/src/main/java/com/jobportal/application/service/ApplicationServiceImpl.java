package com.jobportal.application.service;

import com.itextpdf.text.DocumentException;
import com.jobportal.application.dto.ApplicationDto;
import com.jobportal.application.dto.ListingDto;
import com.jobportal.application.entity.Application;
import com.jobportal.application.enums.ListingStatus;
import com.jobportal.application.exception.ApplicationAlreadyExistsException;
import com.jobportal.application.exception.JobListingNotOpenStatusException;
import com.jobportal.application.exception.JobListingServiceException;
import com.jobportal.application.exception.ResourceNotFoundException;
import com.jobportal.application.kafka.ApplicationProducer;
import com.jobportal.application.mapper.ApplicationMapper;
import com.jobportal.application.repository.ApplicationRepository;
import com.jobportal.application.utility.ApplicationPdfGenerator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;

import java.util.List;


@Service
@AllArgsConstructor
public class ApplicationServiceImpl implements IApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    private final ApplicationRepository applicationRepository;

    private final WebClient webClient;

    private final ApplicationProducer applicationProducer;

    private final ApplicationPdfGenerator applicationPdfGenerator;


    /**
     * @param applicationDto
     */
    @Override
    public void createApplication(ApplicationDto applicationDto) {
        Long jobListingId = applicationDto.getJobListingId();
        logger.debug("Fetching job listing with ID: {}", jobListingId);

        ListingDto listingResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/joblistings/fetch")
                        .queryParam("id", jobListingId)
                        .build())
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse -> {
                    return clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                        logger.error("4xx Error: {}", errorBody);
                        return Mono.error(new ResourceNotFoundException("Job Listing", "Listing Id", jobListingId.toString()));
                    });
                })
                .onStatus(status -> status.is5xxServerError(), clientResponse -> {
                    logger.error("5xx Error while fetching job listing with ID: {}", jobListingId);
                    return Mono.error(new JobListingServiceException("Job listing service is currently unavailable"));
                })
                .bodyToMono(ListingDto.class)
                .block();  // Consider removing this block and switching to reactive

        if (listingResponse == null) {
            throw new JobListingServiceException("No response received from job listing service");
        }

        ListingStatus currentStatus = ListingStatus.valueOf(listingResponse.getListingStatus().toString());

        if (currentStatus != ListingStatus.OPEN) {
            throw new JobListingNotOpenStatusException(
                    String.format("Job listing %d is not open for applications. Current status: %s",
                            jobListingId, listingResponse.getListingStatus())
            );
        }

        Application application = ApplicationMapper.mapToApplication(applicationDto, new Application());
        applicationRepository.save(application);
        applicationProducer.sendApplicationCreatedEvent(jobListingId);
    }


    /**
     * @param applicationId
     * @return
     */
    @Override
    public ApplicationDto getApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "Id", applicationId.toString()));

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

        Application existingApplication = applicationRepository.findById(applicationDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Application", "Id", applicationDto.getId().toString()));

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
                .orElseThrow(() -> new ResourceNotFoundException("Application", "Id", applicationId.toString()));

        applicationRepository.deleteById(applicationId);
        isDeleted = true;

        return isDeleted;
    }

    /**
     * @param jobListingId
     * @return
     * @throws DocumentException
     */

    @Override
    public byte[] getAllApplicationsAsPdf(Long jobListingId) throws DocumentException {
        logger.debug("Fetching all applications for job listing ID: {}", jobListingId);

        // Fetch the job listing
        ListingDto listingResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/joblistings/fetch")
                        .queryParam("id", jobListingId)
                        .build())
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse -> {
                    return clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                        logger.error("4xx Error: {}", errorBody);
                        return Mono.error(new ResourceNotFoundException("Job Listing", "Listing Id", jobListingId.toString()));
                    });
                })
                .onStatus(status -> status.is5xxServerError(), clientResponse -> {
                    logger.error("5xx Error while fetching job listing with ID: {}", jobListingId);
                    return Mono.error(new JobListingServiceException("Job listing service is currently unavailable"));
                })
                .bodyToMono(ListingDto.class)
                .block();

        if (listingResponse == null) {
            throw new ResourceNotFoundException("Job Listing", "Listing Id", jobListingId.toString());
        }

        // Fetch all applications for the job listing
        List<Application> applications = applicationRepository.findByJobListingId(jobListingId);

        if (applications.isEmpty()) {
            throw new ResourceNotFoundException("Applications", "Job Listing Id", jobListingId.toString());
        }

        // Generate PDF
        return applicationPdfGenerator.generateApplicationsPdf(applications);
    }

}

