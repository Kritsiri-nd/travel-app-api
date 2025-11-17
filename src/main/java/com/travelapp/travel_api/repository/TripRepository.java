package com.travelapp.travel_api.repository;

import com.travelapp.travel_api.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository interface for Trip entity
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
}
