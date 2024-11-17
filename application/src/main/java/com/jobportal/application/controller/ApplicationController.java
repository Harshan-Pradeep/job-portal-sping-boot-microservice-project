package com.jobportal.application.controller;

import com.jobportal.application.constants.ApplicationConstants;
import com.jobportal.application.dto.ApplicationDto;
import com.jobportal.application.dto.ResponseDto;
import com.jobportal.application.service.IApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ApplicationController {

    private final IApplicationService iApplicationService;

    public ApplicationController(IApplicationService iApplicationService) {
        this.iApplicationService = iApplicationService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createJobApplication(@Valid @RequestBody ApplicationDto applicationDto) {
        iApplicationService.createApplication(applicationDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(ApplicationConstants.STATUS_201, ApplicationConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<ApplicationDto> fetchJobApplication(@RequestParam Long id) {
        ApplicationDto applicationDto = iApplicationService.getApplication(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(applicationDto);
    }

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


}
