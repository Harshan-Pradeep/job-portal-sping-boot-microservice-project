package com.jobportal.joblistings.repository;

import com.jobportal.joblistings.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing, Long> {

}
