package com.travelapp.travel_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CurrentUserResponse {
    private final Long id;
    private final String email;
    private final String displayName;
}
