package com.travelapp.travel_api.service;

import com.travelapp.travel_api.dto.TripRequest;
import com.travelapp.travel_api.dto.TripResponse;
import com.travelapp.travel_api.entity.Trip;
import com.travelapp.travel_api.entity.User;
import com.travelapp.travel_api.repository.TripRepository;
import com.travelapp.travel_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    // Get all trips or search by query
    public List<TripResponse> getTrips(String query) {
        List<Trip> trips;
        if (query != null && !query.trim().isEmpty()) {
            String keyword = query.trim();
            trips = tripRepository.searchByKeyword(keyword);
        } else {
            trips = tripRepository.findAll();
        }

        return trips.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<TripResponse> searchTrips(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "query is required");
        }
        return getTrips(query);
    }

    // Create new trip
    public TripResponse create(TripRequest req) {
        User author = getCurrentUser();

        Trip trip = new Trip();
        trip.setTitle(req.getTitle());
        trip.setDescription(req.getDescription());
        trip.setPhotos(req.getPhotos() != null ? new ArrayList<>(req.getPhotos()) : new ArrayList<>());
        trip.setTags(req.getTags() != null ? new ArrayList<>(req.getTags()) : new ArrayList<>());
        trip.setLatitude(req.getLatitude());
        trip.setLongitude(req.getLongitude());
        trip.setAuthor(author);
        return toResponse(tripRepository.save(trip));
    }

    // Get trip by id
    public TripResponse getById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
        return toResponse(trip);
    }

    // Update trip by id
    public TripResponse update(Long id, TripRequest req) {
        User author = getCurrentUser();
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
        assertOwnership(trip, author);

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
        User author = getCurrentUser();
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
        assertOwnership(trip, author);
        tripRepository.delete(trip);
    }

    public List<TripResponse> getMyTrips() {
        User author = getCurrentUser();
        return tripRepository.findByAuthorId(author.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
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

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private void assertOwnership(Trip trip, User user) {
        if (trip.getAuthor() == null || user == null || trip.getAuthor().getId() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only modify your own trips");
        }

        if (!trip.getAuthor().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only modify your own trips");
        }
    }
}
