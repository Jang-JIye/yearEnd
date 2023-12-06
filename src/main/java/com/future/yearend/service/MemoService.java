package com.future.yearend.service;

import com.future.yearend.entity.Memo;
import com.future.yearend.dto.MemoRequestDto;
import com.future.yearend.dto.MemoResponseDto;
import com.future.yearend.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;

    public MemoResponseDto createMemo(MemoRequestDto memoRequestDto) {
        Memo memo = memoRepository.save(new Memo(memoRequestDto));
        return new MemoResponseDto(memo);
    }

    public List<MemoResponseDto> getAllMemo() {
        List<Memo> memoList = memoRepository.findAll();
        return memoList.stream().map(MemoResponseDto::new).collect(Collectors.toList());
    }

    public MemoResponseDto getMemo(Long id) {
        return new MemoResponseDto(findMemo(id));
    }


    public ResponseEntity<String> updateMemo(Long id, MemoRequestDto memoRequestDto) {
        Memo memo = findMemo(id);
        memo.update(memoRequestDto);
        return ResponseEntity.ok("수정 성공!");
    }

    public ResponseEntity<String> deleteMemo(Long id) {
        Memo memo = findMemo(id);
        memoRepository.delete(memo);
        return ResponseEntity.ok("삭제 성공!");
    }



    private Memo findMemo(Long id) {
        return memoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 메모는 존재하지 않습니다.")
        );
    }
}
