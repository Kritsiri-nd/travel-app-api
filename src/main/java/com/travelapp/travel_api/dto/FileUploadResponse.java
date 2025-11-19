package com.travelapp.travel_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileUploadResponse {
    private String path;
    private String url;
    private String mimeType;
    private long size;
}
