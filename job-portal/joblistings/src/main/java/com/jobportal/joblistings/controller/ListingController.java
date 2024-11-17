package com.jobportal.joblistings.controller;

import com.itextpdf.text.DocumentException;
import com.jobportal.joblistings.constants.ListingConstants;
import com.jobportal.joblistings.dto.ErrorResponseDto;
import com.jobportal.joblistings.dto.ListingDto;
import com.jobportal.joblistings.dto.ResponseDto;
import com.jobportal.joblistings.entity.Listing;
import com.jobportal.joblistings.service.IListingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CRUD REST APIs for Job Listing Microservice",
        description = "CRUD REST APIs in  User to CREATE, UPDATE, FETCH AND DELETE job listing details"
)
@RestController
@RequestMapping(path = "api/v1/joblistings", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ListingController {

    private final IListingService iListingService;

    public ListingController(IListingService iListingService) {
        this.iListingService = iListingService;
    }

    @Operation(
            summary = "Create Job Listing REST API",
            description = "REST API to create a new Job Listing"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input, job listing could not be created",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createJobListing(@Valid @RequestBody ListingDto listingDto) {
        iListingService.createListing(listingDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(ListingConstants.STATUS_201, ListingConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Fetch Job Listing REST API",
            description = "REST API to fetch a job listing by its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Job listing not found with given ID",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/fetch")
    public ResponseEntity<ListingDto> fetchJobListing(@RequestParam
                                                        Long id) {
        ListingDto listingDto = iListingService.getListing(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listingDto);
    }

    @Operation(
            summary = "Update Job Listing REST API",
            description = "REST API to update an existing job listing"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Job listing not found with given ID",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed. Could not update job listing.",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateJobListing(@Valid @RequestBody ListingDto listingDto) {
        boolean isUpdated = iListingService.updateListing(listingDto);

        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(ListingConstants.STATUS_200, ListingConstants.MESSAGE_200));
        }

        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(ListingConstants.STATUS_417, ListingConstants.MESSAGE_417_UPDATE));
    }

    @Operation(
            summary = "Delete Job Listing REST API",
            description = "REST API to delete a job listing by its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Job listing not found with given ID",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed. Could not delete job listing.",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteJobListing(@RequestParam
                                                            Long id) {
        boolean isDeleted = iListingService.deleteListing(id);

        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(ListingConstants.STATUS_200, ListingConstants.MESSAGE_200));
        }

        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(ListingConstants.STATUS_417, ListingConstants.MESSAGE_417_DELETE));
    }

    @Operation(
            summary = "Export Job Listings as PDF REST API",
            description = "REST API to export all job listings as a PDF document"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error. Could not generate PDF.",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping(value = "/export-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportListingsPdf() {
        try {
            byte[] pdfBytes = iListingService.getAllListingsAsPdf();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "job-listings.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (DocumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
