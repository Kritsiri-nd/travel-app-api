package com.travelapp.travel_api.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {

    @Size(max = 100, message = "displayName must be at most 100 characters")
    private String displayName;

    @Size(max = 2048, message = "avatarUrl is too long")
    private String avatarUrl;

    @Size(max = 2000, message = "bio is too long")
    private String bio;
}
