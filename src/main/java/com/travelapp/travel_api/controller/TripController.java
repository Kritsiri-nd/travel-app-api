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

    //method get all
    @GetMapping //api/trips
    public List<TripResponse> getAll(@RequestParam(value = "query", required = false) String query) {
        return tripService.getTrips(query);
    }

    //method post
    @PostMapping //api/trips
    public TripResponse create(@RequestBody TripRequest req) {
        return tripService.create(req);
    }

    //method get by id
    @GetMapping("/{id}") //api/trips/{id}
    public TripResponse getById(@PathVariable Long id) {
        return tripService.getById(id);
    }

    //method put
    @PutMapping("/{id}") //api/trips/{id}
    public TripResponse update(@PathVariable Long id, @RequestBody TripRequest req) {
        return tripService.update(id, req);
    }
    //method delete
    @DeleteMapping("/{id}") //api/trips/{id}
    public void delete(@PathVariable Long id) {
        tripService.delete(id);
    }

}
