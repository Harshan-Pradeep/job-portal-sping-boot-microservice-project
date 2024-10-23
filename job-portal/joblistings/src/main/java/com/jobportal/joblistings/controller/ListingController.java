package com.jobportal.joblistings.controller;

import com.jobportal.joblistings.constants.ListingConstants;
import com.jobportal.joblistings.dto.ListingDto;
import com.jobportal.joblistings.dto.ResponseDto;
import com.jobportal.joblistings.entity.Listing;
import com.jobportal.joblistings.service.IListingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/joblistings", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ListingController {

    private final IListingService iListingService;

    public ListingController(IListingService iListingService) {
        this.iListingService = iListingService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createJobListing(@Valid @RequestBody ListingDto listingDto) {
        iListingService.createListing(listingDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(ListingConstants.STATUS_201, ListingConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<ListingDto> fetchJobListing(@RequestParam
                                                        Long id) {
        ListingDto listingDto = iListingService.getListing(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listingDto);
    }

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
}
