package com.travelapp.travel_api.dto;

import lombok.Data;

@Data
public class TripRequest {
    private String title;
    private String description;
    private String[] photos;
    private String[] tags;
    private Double latitude;
    private Double longitude;
}
