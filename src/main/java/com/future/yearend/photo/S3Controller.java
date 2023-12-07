package com.future.yearend.photo;

import com.future.yearend.security.UserDetailsImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping("/api/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam MultipartFile file, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return s3Service.uploadPhoto(file, userDetails.getUsername());
    }

    @GetMapping("/api/photo")
    public List<Photo> getPhotos() {
        return s3Service.getPhotos();
    }

    @GetMapping("/api/photo/{id}")
    public Optional<Photo> getPhoto(@PathVariable Long id) {
        return s3Service.getPhoto(id);
    }

    @DeleteMapping("/api/photo/{id}")
    public ResponseEntity<String> deletePhoto(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return s3Service.deletePhoto(id, userDetails.getUsername());
    }
}
