package com.jobportal.joblistings.service;

import com.jobportal.joblistings.dto.ListingDto;
import com.jobportal.joblistings.entity.Listing;
import com.jobportal.joblistings.exception.ResourceNotFoundException;
import com.jobportal.joblistings.mapper.ListingMapper;
import com.jobportal.joblistings.repository.ListingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ListingServiceImpl implements IListingService{

    private ListingRepository listingRepository;


    /**
     * @param listingDto
     */
    @Override
    public void createListing(ListingDto listingDto) {
        Listing listing = ListingMapper.mapToListing(listingDto, new Listing());
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
}
