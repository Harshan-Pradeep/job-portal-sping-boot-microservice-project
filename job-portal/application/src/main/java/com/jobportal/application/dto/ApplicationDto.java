package com.jobportal.application.dto;

import com.jobportal.application.enums.ApplicationStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDto {

    private Long id;

    @NotNull
    private Long jobListingId;

    @NotNull
    private Long applicantId;

    @NotNull
    private ApplicationStatus status;

    private String resumeUrl;

    private String coverLetter;
}
