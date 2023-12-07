package com.future.yearend.memo;

import com.future.yearend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @PostMapping("/year-end")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto memoRequestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memoService.createMemo(memoRequestDto, userDetails.getUsername());
    }

    @GetMapping("/year-end")
    public List<MemoResponseDto> getAllMemo() {
        return memoService.getAllMemo();
    }

    @GetMapping("/year-end/{id}")
    public MemoResponseDto getMemo(@PathVariable Long id) {
        return memoService.getMemo(id);
    }

    @PutMapping("/year-end/{id}")
    public ResponseEntity<String> updateMemo(@PathVariable Long id,
                                             @RequestBody MemoRequestDto memoRequestDto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memoService.updateMemo(id, memoRequestDto, userDetails.getUsername());
    }

    @DeleteMapping("/year-end/{id}")
    public ResponseEntity<String> deleteMemo(@PathVariable Long id,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memoService.deleteMemo(id, userDetails.getUsername());
    }
}
