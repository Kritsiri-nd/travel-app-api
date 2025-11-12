package com.travelapp.travel_api.repository;

import com.travelapp.travel_api.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository interface for Trip entity
public interface TripRepository extends JpaRepository<Trip, Long> {
}
