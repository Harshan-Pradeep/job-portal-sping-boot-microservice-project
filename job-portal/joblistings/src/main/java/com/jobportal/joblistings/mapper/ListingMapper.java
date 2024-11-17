package com.jobportal.joblistings.mapper;

import com.jobportal.joblistings.dto.ListingDto;
import com.jobportal.joblistings.entity.Listing;

public class ListingMapper {
    public static ListingDto mapToListingDto(Listing listing, ListingDto listingDto) {
        listingDto.setId(listing.getId());
        listingDto.setTitle(listing.getTitle());
        listingDto.setDescription(listing.getDescription());
        listingDto.setLocation(listing.getLocation());
        listingDto.setCompany(listing.getCompany());
        listingDto.setSalaryRange(listing.getSalaryRange());
        listingDto.setExperienceLevel(listing.getExperienceLevel());
        listingDto.setJobType(listing.getJobType());
        listingDto.setListingStatus(listing.getListingStatus());
        return listingDto;
    }

    public static Listing mapToListing(ListingDto listingDto, Listing listing) {
        listing.setId(listingDto.getId());
        listing.setTitle(listingDto.getTitle());
        listing.setDescription(listingDto.getDescription());
        listing.setLocation(listingDto.getLocation());
        listing.setCompany(listingDto.getCompany());
        listing.setSalaryRange(listingDto.getSalaryRange());
        listing.setExperienceLevel(listingDto.getExperienceLevel());
        listing.setJobType(listingDto.getJobType());
        listing.setListingStatus(listingDto.getListingStatus());
        return listing;
    }
}
