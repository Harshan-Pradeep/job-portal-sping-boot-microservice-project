package com.jobportal.application.controller;

import com.itextpdf.text.DocumentException;
import com.jobportal.application.constants.ApplicationConstants;
import com.jobportal.application.dto.ApplicationDto;
import com.jobportal.application.dto.ResponseDto;
import com.jobportal.application.exception.ApplicationAlreadyExistsException;
import com.jobportal.application.exception.ResourceNotFoundException;
import com.jobportal.application.kafka.ApplicationProducer;
import com.jobportal.application.service.IApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Tag(
        name = "CRUD REST APIs for Job Application Microservice",
        description = "CRUD REST APIs in  User to CREATE, UPDATE, FETCH AND DELETE job application details"
)
@RestController
@RequestMapping(path = "api/v1/applications", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ApplicationController {

    private final IApplicationService iApplicationService;
    private final ApplicationProducer applicationProducer;

    public ApplicationController(IApplicationService iApplicationService, ApplicationProducer applicationProducer) {
        this.iApplicationService = iApplicationService;
        this.applicationProducer = applicationProducer;
    }

    @Operation(
            summary = "Create a Job Application",
            description = "REST API to create a new job application."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Job application created successfully."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request. Application already exists or job listing not found."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error. An unexpected error occurred."
            )
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createJobApplication(@Valid @RequestBody ApplicationDto applicationDto) {
        try {
            iApplicationService.createApplication(applicationDto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseDto(ApplicationConstants.STATUS_201, ApplicationConstants.MESSAGE_201));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto(ApplicationConstants.STATUS_400, e.getMessage()));
        } catch (ApplicationAlreadyExistsException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto(ApplicationConstants.STATUS_400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(ApplicationConstants.STATUS_500, "An unexpected error occurred"));
        }
    }


    @Operation(
            summary = "Fetch a Job Application",
            description = "REST API to fetch a job application by its ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Job application retrieved successfully."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Job application not found."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error. An unexpected error occurred."
            )
    })
    @GetMapping("/fetch")
    public ResponseEntity<ApplicationDto> fetchJobApplication(@RequestParam Long id) {
        try {
            ApplicationDto applicationDto = iApplicationService.getApplication(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(applicationDto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    @Operation(
            summary = "Update a Job Application",
            description = "REST API to update an existing job application."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Job application updated successfully."
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed. Update was not successful."
            )
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateJobApplication(@Valid @RequestBody ApplicationDto applicationDto) {
        boolean isUpdated = iApplicationService.updateApplication(applicationDto);

        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(ApplicationConstants.STATUS_200, ApplicationConstants.MESSAGE_200));
        }

        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(ApplicationConstants.STATUS_417, ApplicationConstants.MESSAGE_417_UPDATE));
    }

    @Operation(
            summary = "Delete a Job Application",
            description = "REST API to delete a job application by its ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Job application deleted successfully."
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed. Deletion was not successful."
            )
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteJobApplication(@RequestParam Long id) {
        boolean isDeleted = iApplicationService.deleteApplication(id);

        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(ApplicationConstants.STATUS_200, ApplicationConstants.MESSAGE_200));
        }

        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(ApplicationConstants.STATUS_417, ApplicationConstants.MESSAGE_417_DELETE));
    }

    @Operation(
            summary = "Get All Applications as PDF",
            description = "REST API to fetch all applications for a specific job listing as a PDF document."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "PDF document with applications retrieved successfully.",
                    content = @Content(mediaType = MediaType.APPLICATION_PDF_VALUE)
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Job listing or applications not found."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error. Could not generate PDF."
            )
    })
    @GetMapping(value = "/report", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getAllApplicationsAsPdf(@RequestParam Long jobListingId) {
        try {
            byte[] pdfContent = iApplicationService.getAllApplicationsAsPdf(jobListingId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("applications_report.pdf")
                    .build());

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(pdfContent);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (DocumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
