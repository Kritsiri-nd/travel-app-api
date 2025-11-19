package com.travelapp.travel_api.service;

import com.travelapp.travel_api.dto.FileUploadResponse;
import com.travelapp.travel_api.exception.BadRequestException;
import com.travelapp.travel_api.exception.InternalServerException;
import com.travelapp.travel_api.exception.StorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private static final long MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024; // 5 MB
    private static final List<String> ALLOWED_TYPES = List.of(
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            "image/webp"
    );

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.bucket}")
    private String bucketName;

    @Value("${supabase.apiKey}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public FileUploadResponse uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("file is required");
        }
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new BadRequestException("file size exceeds 5 MB limit");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new BadRequestException("unsupported file type");
        }

        assertSupabaseConfig();

        String path = "images/" + UUID.randomUUID() + fileExtension(contentType);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setBearerAuth(apiKey);
        headers.set("apikey", apiKey);
        headers.set("x-upsert", "true");

        String uploadUrl = buildUploadUrl(path);

        try {
            RequestEntity<byte[]> requestEntity = new RequestEntity<>(
                    file.getBytes(),
                    headers,
                    HttpMethod.POST,
                    URI.create(uploadUrl)
            );
            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new StorageException("failed to upload file to storage");
            }
        } catch (IOException ex) {
            throw new InternalServerException("unable to read uploaded file", ex);
        } catch (RestClientResponseException ex) {
            throw new StorageException("Supabase rejected the upload: " + ex.getStatusText(), ex);
        } catch (RestClientException ex) {
            throw new StorageException("failed to upload file to storage", ex);
        }

        return new FileUploadResponse(path, buildPublicUrl(path), contentType, file.getSize());
    }

    private void assertSupabaseConfig() {
        if (isBlank(supabaseUrl) || isBlank(bucketName) || isBlank(apiKey)) {
            throw new InternalServerException("Supabase configuration is missing");
        }
    }

    private String fileExtension(String type) {
        return switch (type) {
            case MediaType.IMAGE_PNG_VALUE -> ".png";
            case "image/webp" -> ".webp";
            default -> ".jpg";
        };
    }

    private String buildUploadUrl(String path) {
        String base = supabaseUrl.endsWith("/") ? supabaseUrl.substring(0, supabaseUrl.length() - 1) : supabaseUrl;
        return base + "/storage/v1/object/" + bucketName + "/" + path;
    }

    private String buildPublicUrl(String path) {
        String base = supabaseUrl.endsWith("/") ? supabaseUrl.substring(0, supabaseUrl.length() - 1) : supabaseUrl;
        return base + "/storage/v1/object/public/" + bucketName + "/" + path;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
