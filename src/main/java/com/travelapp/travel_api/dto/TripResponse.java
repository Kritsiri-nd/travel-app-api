package com.travelapp.travel_api.dto;

import lombok.Data;
import java.time.OffsetDateTime;
import java.util.List;

@Data 
public class TripResponse {
    private Long id;
    private String title;
    private String description;
    private List<String> photos;
    private List<String> tags;
    private Double latitude;
    private Double longitude;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
} // DTO สำหรับการส่งข้อมูลทริปกลับไปยังผู้ใช้
