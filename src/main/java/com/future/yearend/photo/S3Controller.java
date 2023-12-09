package com.future.yearend.photo;

import com.future.yearend.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "사진 관련 API", description = "사진 관련 API")
@CrossOrigin(origins = "http://localhost:5173")
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping("/api/photo")
    @Operation(summary = "사진 등록", description = "사진 등록 API 입니다.")
    public ResponseEntity<String> uploadPhoto(@RequestParam MultipartFile file, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return s3Service.uploadPhoto(file, userDetails.getUsername());
    }

    @GetMapping("/api/photo")
    @Operation(summary = "사진 12개 조회", description = "사진 12개 최신 조회 API 입니다.")
    public List<PhotoResponseDto> getPhotos() {
        return s3Service.getPhotos();
    }

    @GetMapping("/api/photo/{id}")
    @Operation(summary = "사진 개별 조회", description = "사진 개별 조회 API 입니다.")
    public ResponseEntity<PhotoResponseDto> getPhoto(@PathVariable Long id) {
        return s3Service.getPhoto(id);
    }

    @DeleteMapping("/api/photo/{id}")
    @Operation(summary = "사진 삭제", description = "사진 삭제 API 입니다.")
    public ResponseEntity<String> deletePhoto(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return s3Service.deletePhoto(id, userDetails.getUsername());
    }
}
