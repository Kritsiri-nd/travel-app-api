package com.travelapp.travel_api.service;

import com.travelapp.travel_api.dto.TripRequest;
import com.travelapp.travel_api.dto.TripResponse;
import com.travelapp.travel_api.entity.Trip;
import com.travelapp.travel_api.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;

    public List<TripResponse> getAll() {
        return tripRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TripResponse create(TripRequest req) {
        Trip trip = new Trip();
        trip.setTitle(req.getTitle());
        trip.setDescription(req.getDescription());
        trip.setPhotos(req.getPhotos());
        trip.setTags(req.getTags());
        trip.setLatitude(req.getLatitude());
        trip.setLongitude(req.getLongitude());
        return toResponse(tripRepository.save(trip));
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
    }
}
