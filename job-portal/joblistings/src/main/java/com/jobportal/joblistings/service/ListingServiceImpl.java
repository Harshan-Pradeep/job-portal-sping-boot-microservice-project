package com.jobportal.joblistings.service;

import com.itextpdf.text.DocumentException;
import com.jobportal.joblistings.dto.ListingDto;
import com.jobportal.joblistings.entity.Listing;
import com.jobportal.joblistings.enums.ListingStatus;
import com.jobportal.joblistings.exception.ResourceNotFoundException;
import com.jobportal.joblistings.mapper.ListingMapper;
import com.jobportal.joblistings.repository.ListingRepository;
import com.jobportal.joblistings.utility.PdfGenerator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListingServiceImpl implements IListingService{

    private final ListingRepository listingRepository;
    private final PdfGenerator pdfGenerator;
    private static final Logger logger = LoggerFactory.getLogger(ListingServiceImpl.class);

    public ListingServiceImpl(ListingRepository listingRepository, PdfGenerator pdfGenerator) {
        this.listingRepository = listingRepository;
        this.pdfGenerator = pdfGenerator;
    }

    /**
     * @param listingDto
     */
    @Override
    public void createListing(ListingDto listingDto) {
        Listing listing = ListingMapper.mapToListing(listingDto, new Listing());
        listing.setListingStatus(ListingStatus.OPEN);
        listingRepository.save(listing);
    }

    /**
     * @param listingDto
     * @return
     */
    @Override
    public boolean updateListing(ListingDto listingDto) {
        boolean isUpdated = false;

        Listing existingListing = listingRepository.findById(listingDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Listing " ,"Id", listingDto.getId().toString() ));

        Listing updatedListing = ListingMapper.mapToListing(listingDto, existingListing);
        listingRepository.save(updatedListing);
        isUpdated = true;

        return isUpdated;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public boolean deleteListing(Long id) {
        boolean isDeleted = false;

        listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing " ,"Id", id.toString() ));

        listingRepository.deleteById(id);
        isDeleted = true;

        return isDeleted;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ListingDto getListing(Long id) {

        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing " ,"Id", id.toString() ));

        ListingDto listingDto = ListingMapper.mapToListingDto(listing, new ListingDto());

        return listingDto;
    }

    /**
     * @return
     * @throws DocumentException
     */
    @Override
    public byte[] getAllListingsAsPdf() throws DocumentException {
        List<Listing> listings = listingRepository.findAll();
        return pdfGenerator.generateListingsPdf(listings);
    }

    /**
     * @param listingId
     */
    @Override
    @Transactional
    public void incrementApplicationCount(Long listingId) {
        logger.info("incrementApplicationCount called for listingId: {}", listingId);
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Listing", "Id", listingId.toString()));

        int currentCount = listing.getApplicationsCount();
        logger.info("Current application count for listing {}: {}", listingId, currentCount);

        listing.setApplicationsCount(currentCount + 1);

        listingRepository.save(listing);
        logger.info("Updated application count for listing {}: new count {}", listingId, listing.getApplicationsCount());
    }

}
