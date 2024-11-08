package com.jobportal.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class JobListingNotOpenStatusException extends RuntimeException{
    public JobListingNotOpenStatusException(String message) {
        super(message);
    }
}
