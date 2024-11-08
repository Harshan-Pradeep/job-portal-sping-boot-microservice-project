package com.jobportal.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
public class JobListingServiceException extends RuntimeException {
    public JobListingServiceException(String message) {
        super(message);
    }
}
