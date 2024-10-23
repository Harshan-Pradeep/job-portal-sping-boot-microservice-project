package com.jobportal.application.service;

import com.jobportal.application.dto.ApplicationDto;
import com.jobportal.application.entity.Application;
import com.jobportal.application.exception.ApplicationAlreadyExistsException;
import com.jobportal.application.exception.ResourceNotFoundException;
import com.jobportal.application.mapper.ApplicationMapper;
import com.jobportal.application.repository.ApplicationRepository;
import com.jobportal.joblistings.dto.ListingDto;
import com.jobportal.joblistings.enums.ListingStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;


@Service
@AllArgsConstructor
public class ApplicationServiceImpl implements IApplicationService{

    private final ApplicationRepository applicationRepository;

    private final WebClient webClient;


    /**
     * @param applicationDto
     */
    @Override
    public void createApplication(ApplicationDto applicationDto) {
        Long jobListingId = applicationDto.getJobListingId();
        System.out.println("Fetching job listing with ID: " + jobListingId); // Debug statement

        try {
            ListingDto listingResponse = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/api/v1/joblistings/fetch")
                            .queryParam("id", jobListingId) // Add the query parameter
                            .build()) // Build the URI
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(), clientResponse -> {
                        return clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                            System.err.println("4xx Error: " + errorBody); // Log error body
                            return Mono.error(new ResourceNotFoundException("Job Listing", "Listing Id", jobListingId.toString()));
                        });
                    })
                    .onStatus(status -> status.is5xxServerError(), clientResponse ->
                            Mono.error(new Exception("Server error while fetching Job Listing"))
                    )
                    .bodyToMono(ListingDto.class)
                    .block();

            if (listingResponse.getListingStatus() == ListingStatus.OPEN) {
                Application application = ApplicationMapper.mapToApplication(applicationDto, new Application());
                applicationRepository.save(application);
            } else {
                throw new ResourceNotFoundException("Job Listing", "Listing Id", jobListingId.toString());
            }
        } catch (Exception e) {
            // Log the exception for further debugging
            e.printStackTrace();
            throw new RuntimeException("Error occurred while creating application");
        }
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
