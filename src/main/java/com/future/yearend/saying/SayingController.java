package com.future.yearend.saying;

import com.amazonaws.services.kms.model.ReEncryptRequest;
import com.future.yearend.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "명언 API", description = "명언 API")
public class SayingController {
    private final SayingService sayingService;

    @PostMapping("/api/saying")
    @Operation(summary = "명언 등록", description = "명언 등록 API 입니다.")
    public SayingResponseDto createSaying(@RequestBody SayingRequestDto sayingRequestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return sayingService.createSaying(sayingRequestDto, userDetails.getUser());
    }

    @GetMapping("/api/saying")
    @Operation(summary = "명언 랜덤 조회", description = "명언 랜덤 조회 API 입니다.")
    public ResponseEntity<SayingResponseDto> getRandomSaying() {
        return sayingService.getRandomSaying();
    }

    @DeleteMapping("api/saying/{id}")
    @Operation(summary = "명언 삭제", description = "명언 삭제 API 입니다.")
    public ResponseEntity<String> deleteSaying(@PathVariable Long id,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return sayingService.deleteSaying(id, userDetails.getUser());
    }
}
