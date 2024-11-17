package com.jobportal.joblistings.service;

import com.jobportal.joblistings.dto.ListingDto;

public interface IListingService {

    void createListing(ListingDto listingDto);

    boolean updateListing(ListingDto listingDto);

    boolean deleteListing(Long id);

    ListingDto getListing(Long id);

}
