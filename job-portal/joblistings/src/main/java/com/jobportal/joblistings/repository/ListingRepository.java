package com.jobportal.joblistings.repository;

import com.jobportal.joblistings.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListingRepository extends JpaRepository<Listing, Long> {

    List<Listing> findAll();

}
