package com.jobportal.joblistings.service;

import com.itextpdf.text.DocumentException;
import com.jobportal.joblistings.dto.ListingDto;

public interface IListingService {

    void createListing(ListingDto listingDto);

    boolean updateListing(ListingDto listingDto);

    boolean deleteListing(Long id);

    ListingDto getListing(Long id);

    byte[] getAllListingsAsPdf() throws DocumentException;

    void incrementApplicationCount(Long listingId);

}
