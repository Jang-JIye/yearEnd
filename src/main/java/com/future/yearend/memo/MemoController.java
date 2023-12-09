package com.future.yearend.memo;

import com.future.yearend.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "캘린더 관련 API", description = "캘린더 소망 관련 API 입니다.")
public class MemoController {

    private final MemoService memoService;

    @PostMapping("/year-end")
    @Operation(summary = "소망 작성", description = "소망 작성 API 입니다.")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto memoRequestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memoService.createMemo(memoRequestDto, userDetails.getUsername());
    }

    @GetMapping("/year-end")
    @Operation(summary = "소망 전체 조회", description = "소망 전체 조회 API 입니다.")
    public List<MemoResponseDto> getAllMemo() {
        return memoService.getAllMemo();
    }

    @GetMapping("/year-end/{id}")
    @Operation(summary = "소망 개별 조회", description = "소망 개별 조회 API 입니다.")
    public MemoResponseDto getMemo(@PathVariable Long id) {
        return memoService.getMemo(id);
    }

    @PutMapping("/year-end/{id}")
    @Operation(summary = "소망 수정", description = "소망 수정 API 입니다.")
    public ResponseEntity<String> updateMemo(@PathVariable Long id,
                                             @RequestBody MemoRequestDto memoRequestDto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memoService.updateMemo(id, memoRequestDto, userDetails.getUsername());
    }

    @DeleteMapping("/year-end/{id}")
    @Operation(summary = "소망 삭제", description = "소망 삭제 API 입니다.")
    public ResponseEntity<String> deleteMemo(@PathVariable Long id,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memoService.deleteMemo(id, userDetails.getUsername());
    }

    @GetMapping("/year-end/month/{month}")
    @Operation(summary = "소망 월별 조회", description = "소망 월별 조회 API 입니다.")
    public List<MemoResponseDto> getMonthMemo(@PathVariable String month) {
        return memoService.getMonthMemo(month);
    }

    @GetMapping("/year-end/month/{month}/day/{day}")
    @Operation(summary = "소망 일별 조회", description = "소망 일별 조회 API 입니다.")
    public List<MemoResponseDto> getDayMemo(@PathVariable String month,
                                            @PathVariable String day) {
        return memoService.getDayMemo(month, day);
    }

    @GetMapping("/year-end/user-memo")
    @Operation(summary = "사용자별 소망 조회", description = "사용자별 소망 조회 API 입니다.")
    public List<MemoResponseDto> getUserMemo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memoService.getUserMemo(userDetails.getUser());
    }
}
