package com.travelapp.travel_api.service;

import com.travelapp.travel_api.dto.TripRequest;
import com.travelapp.travel_api.dto.TripResponse;
import com.travelapp.travel_api.entity.Trip;
import com.travelapp.travel_api.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    // Get all trips
    public List<TripResponse> getAll() {
        return tripRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Create new trip
    public TripResponse create(TripRequest req) { 
        Trip trip = new Trip();
        trip.setTitle(req.getTitle());
        trip.setDescription(req.getDescription());
        trip.setPhotos(req.getPhotos() != null ? new ArrayList<>(req.getPhotos()) : new ArrayList<>());
        trip.setTags(req.getTags() != null ? new ArrayList<>(req.getTags()) : new ArrayList<>());
        trip.setLatitude(req.getLatitude());
        trip.setLongitude(req.getLongitude());
        return toResponse(tripRepository.save(trip));
    }

    // Get trip by id
    public TripResponse getById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found with id: " + id));
        return toResponse(trip);
    }

    // Update trip by id
    public TripResponse update(Long id, TripRequest req) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip not found with id: " + id));

        if (req.getTitle() != null) trip.setTitle(req.getTitle());
        if (req.getDescription() != null) trip.setDescription(req.getDescription());
        if (req.getPhotos() != null) trip.setPhotos(new ArrayList<>(req.getPhotos()));
        if (req.getTags() != null) trip.setTags(new ArrayList<>(req.getTags()));
        if (req.getLatitude() != null) trip.setLatitude(req.getLatitude());
        if (req.getLongitude() != null) trip.setLongitude(req.getLongitude());

        return toResponse(tripRepository.save(trip));
    }

    // Delete trip by id
    public void delete(Long id) {
        if (!tripRepository.existsById(id)) {
            throw new RuntimeException("Trip not found with id: " + id);
        }
        tripRepository.deleteById(id);
    } 

    private TripResponse toResponse(Trip trip) {
        TripResponse dto = new TripResponse();
        dto.setId(trip.getId());
        dto.setTitle(trip.getTitle());
        dto.setDescription(trip.getDescription());
        dto.setPhotos(trip.getPhotos());
        dto.setTags(trip.getTags());
        dto.setLatitude(trip.getLatitude());
        dto.setLongitude(trip.getLongitude());
        dto.setCreatedAt(trip.getCreatedAt());
        dto.setUpdatedAt(trip.getUpdatedAt());
        return dto;
    } // แปลง Entity เป็น DTO
}
