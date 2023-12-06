package com.future.yearend.controller;

import com.future.yearend.dto.MemoRequestDto;
import com.future.yearend.dto.MemoResponseDto;
import com.future.yearend.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @PostMapping("/year-end")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto memoRequestDto) {
        return memoService.createMemo(memoRequestDto);
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
    public ResponseEntity<String> updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto memoRequestDto) {
        return memoService.updateMemo(id, memoRequestDto);
    }

    @DeleteMapping("/year-end/{id}")
    public ResponseEntity<String> deleteMemo(@PathVariable Long id) {
        return memoService.deleteMemo(id);
    }
}
