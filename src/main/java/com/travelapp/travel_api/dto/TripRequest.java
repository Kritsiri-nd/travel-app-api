package com.travelapp.travel_api.dto;

import lombok.Data;
import java.util.List;

@Data
public class TripRequest {
    private String title;
    private String description;
    private List<String> photos;
    private List<String> tags;
    private Double latitude;
    private Double longitude;
} // DTO สำหรับการรับข้อมูลการสร้างหรืออัปเดตทริป
