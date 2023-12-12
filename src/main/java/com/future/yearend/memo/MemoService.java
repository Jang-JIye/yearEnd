package com.future.yearend.memo;

import com.future.yearend.common.UserRoleEnum;
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
        String nickname = memoRequestDto.getNickname();
        if (memoRequestDto.getNickname() == null) {
            nickname = createRandomNickName();
        }
        Memo memo = new Memo(user.getUsername(), user.getPhoneNum(), nickname,
                memoRequestDto.getContents(), memoRequestDto.getMonth(), memoRequestDto.getDay(), user);

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

        if (user.getUserRole().equals(UserRoleEnum.USER) && !memo.getUser().getUsername().equals(username)) {
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

        if (user.getUserRole().equals(UserRoleEnum.USER) && !memo.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("작성자가 다릅니다.");
        }

        memoRepository.delete(memo);
        return ResponseEntity.ok("삭제 성공!");
    }

    public List<MemoResponseDto> getMonthMemo(String month) {
        List<Memo> monthMemoList = memoRepository.findAllByMonth(month);
        return monthMemoList.stream().map(MemoResponseDto::new).collect(Collectors.toList());
    }

    public List<MemoResponseDto> getDayMemo(String month,String day) {
        List<Memo> dayMemoList = memoRepository.findAllByMonthAndDay(month, day);
        return dayMemoList.stream().map(MemoResponseDto::new).collect(Collectors.toList());    }

    public List<MemoResponseDto> getUserMemo(User user) {
        User existUser = findUser(user.getUsername());
        List<Memo> userMemoList = memoRepository.findAllByUser(existUser);

        return userMemoList.stream().map(MemoResponseDto::new).collect(Collectors.toList());
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

    // 랜덤 닉네임 생성 메서드
    public String createRandomNickName() {
        String[] nickname1 =
                {"행복한", "즐거운", "평화로운", "요망진", "귀여운", "화가난", "달리는", "잠자는", "놀고있는", "하늘을 나는", "여행가고싶은", "영앤리치", "바쁘다바빠!", "하고싶은 말이 많은", "어른스러운", "깜찍한", "엉뚱한", "소심한", "화이팅!", "두둠칫"};
        String[] nickname2 =
                {"강아지", "고양이", "곰", "여우", "오리", "햄스터", "다람쥐", "돌고래", "패럿", "앵무새", "노루"};

        int maxCreateRandomNickName = nickname1.length * nickname2.length; // 경우의 수
        int maxTries = 300;
        for (int tries = 0; tries < maxTries; tries++) {
            int random1 = (int) (Math.random() * nickname1.length);
            int random2 = (int) (Math.random() * nickname2.length);

            String randomNickname = nickname1[random1] + " " + nickname2[random2];

            if (!memoRepository.existsByNickname(randomNickname)) {
                return randomNickname;
            }
        }
        throw new IllegalArgumentException("닉네임 생성에 실패하였습니다.");
    }
}
