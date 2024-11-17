package com.jobportal.joblistings.dto;

import com.jobportal.joblistings.enums.JobType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListingDto {

    private Long id;

    @NotEmpty
    private String title;

    private String description;

    @NotEmpty
    private String company;

    private String location;

    private String salaryRange;

    private String experienceLevel;

    @NotNull
    private JobType jobType;

}
