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
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping("/api/photo")
    @Operation(summary = "사진 등록", description = "사진 등록 API 입니다.")
    public ResponseEntity<String> uploadPhoto(@RequestParam(value = "file", required = false) MultipartFile file,
                                              @RequestParam("month")String month,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return s3Service.uploadPhoto(file, month, userDetails.getUser());
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

    @GetMapping("/api/photo/month/{month}")
    @Operation(summary = "사진 월별 조회", description = "사진 월별 조회 API 입니다.")
    public List<PhotoResponseDto> getMonthPhotos(@PathVariable String month)
                                                  {
        return s3Service.getMonthPhotos(month);
    }

    @GetMapping("/api/photo/last-photos")
    @Operation(summary = "월별 최신 사진 리스트 조회", description = "월별 최신 사진 리스트 조회 API 입니다.")
    public List<PhotoResponseDto> getLatestPhotoOfEachMonth() {
        return s3Service.getLatestPhotoOfEachMonth();
    }

    @GetMapping("/api/photo/user-photo")
    @Operation(summary = "사용자별 사진 조회", description = "내가 올린 사진 조회 API 입니다.")
    public List<PhotoResponseDto> getUserPhotos(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return s3Service.getUserPhotos(userDetails.getUser());
    }


    @DeleteMapping("/api/photo/{id}")
    @Operation(summary = "사진 삭제", description = "사진 삭제 API 입니다.")
    public ResponseEntity<String> deletePhoto(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return s3Service.deletePhoto(id, userDetails.getUser());
    }
}
