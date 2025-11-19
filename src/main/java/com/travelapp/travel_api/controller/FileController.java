package com.travelapp.travel_api.controller;

import com.travelapp.travel_api.dto.FileUploadResponse;
import com.travelapp.travel_api.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileUploadService fileUploadService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public FileUploadResponse uploadImage(@RequestParam("file") MultipartFile file) {
        return fileUploadService.uploadImage(file);
    }
}
