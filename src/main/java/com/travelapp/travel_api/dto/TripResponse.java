package com.travelapp.travel_api.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class TripResponse {
    private Long id;
    private String title;
    private String description;
    private String[] photos;
    private String[] tags;
    private Double latitude;
    private Double longitude;
    private Instant createdAt;
    private Instant updatedAt;
}
