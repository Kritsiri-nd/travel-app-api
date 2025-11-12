package com.travelapp.travel_api.controller;

import com.travelapp.travel_api.dto.TripRequest;
import com.travelapp.travel_api.dto.TripResponse;
import com.travelapp.travel_api.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    @GetMapping
    public List<TripResponse> getAll() {
        return tripService.getAll();
    }

    @PostMapping
    public TripResponse create(@RequestBody TripRequest req) {
        return tripService.create(req);
    }

    @GetMapping("/{id}")
    public TripResponse getById(@PathVariable Long id) {
        return tripService.getById(id);
    }

    @PutMapping("/{id}")
    public TripResponse update(@PathVariable Long id, @RequestBody TripRequest req) {
        return tripService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tripService.delete(id);
    }

}
