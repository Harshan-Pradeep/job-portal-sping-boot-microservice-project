package com.jobportal.joblistings.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, String feildName, String feildValue) {
        super(String.format("%s not found with the given input data %s : '%s'", resourceName, feildName, feildValue));
    }
}
