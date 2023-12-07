package com.future.yearend.memo;

import com.future.yearend.util.JwtUtil;
import com.future.yearend.user.User;
import com.future.yearend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public MemoResponseDto createMemo(MemoRequestDto memoRequestDto, String username) {
        User user = findUser(username);
        Memo memo = new Memo(memoRequestDto, user);

        Memo saveMemo = memoRepository.save(memo);
        return new MemoResponseDto(saveMemo, user);
    }

    public List<MemoResponseDto> getAllMemo() {
        List<Memo> memoList = memoRepository.findAll();
        return memoList.stream().map(MemoResponseDto::new).collect(Collectors.toList());
    }

    public MemoResponseDto getMemo(Long id) {
        return new MemoResponseDto(findMemo(id));
    }


    public ResponseEntity<String> updateMemo(Long id, MemoRequestDto memoRequestDto, String username) {
        Memo memo = findMemo(id);
        User user = findUser(username);

        if (!memo.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("작성자가 다릅니다.");
        }

        // 메모 업데이트
        memo.update(memoRequestDto, user);
        memoRepository.save(memo);
        return ResponseEntity.ok("수정 성공!");
    }

    public ResponseEntity<String> deleteMemo(Long id, String username) {
        Memo memo = findMemo(id);
        User user = findUser(username);

        if (!memo.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("작성자가 다릅니다.");
        }

        memoRepository.delete(memo);
        return ResponseEntity.ok("삭제 성공!");
    }



    private Memo findMemo(Long id) {
        return memoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 메모는 존재하지 않습니다.")
        );
    }
    private User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                ()-> new IllegalArgumentException("해당 작성자는 존재하지 않습니다.")
        );
    }
}