package com.jobportal.joblistings.dto;

import com.jobportal.joblistings.enums.JobType;
import com.jobportal.joblistings.enums.ListingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "Job Listings",
        description = "Schema to hold Job Listing information"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListingDto {

    private Long id;

    @NotEmpty
    @Schema(
            description = "Title of job listing", example = "Software Engineer - Node Js"
    )
    private String title;

    @Schema(
            description = "Description of job listing", example = "3+ year industry experience"
    )
    private String description;

    @NotEmpty
    @Schema(
            description = "Company of job listing", example = "ABC"
    )
    private String company;

    @Schema(
            description = "Location of job listing", example = "Colombo"
    )
    private String location;

    @Schema(
            description = "Salary Range of job listing", example = "1500-3000 USD per year"
    )
    private String salaryRange;

    @Schema(
            description = "Experience Level of job listing", example = "3+ years"
    )
    private String experienceLevel;

    @NotNull
    @Schema(
            description = "Job Type of job listing", example = "FULL_TIME"
    )
    private JobType jobType;

    @Schema(
            description = "Listing Status of job listing", example = "OPEN"
    )
    private ListingStatus listingStatus;

}
