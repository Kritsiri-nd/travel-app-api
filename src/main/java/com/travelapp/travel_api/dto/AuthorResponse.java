package com.travelapp.travel_api.dto;

import lombok.Data;

@Data
public class AuthorResponse {
    private String name;
    private String avatarUrl;
    private String bio;
}
