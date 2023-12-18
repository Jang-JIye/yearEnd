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
                {"행복한", "배고픈", "배부른", "평화주의", "야호!", "꿈꾸는", "화가 난", "달려라", "꿈꾸는", "노는게 제일 좋은", "하늘을 나는", "여행 가고 싶은", "영 앤 리치", "바쁘다 바빠!", "하고 싶은 말이 많은", "어른스러운", "깜찍한", "엉뚱한", "소심한", "화이팅!", "두둠칫", "용감한", "판타스틱", "메롱~ ", "슬기로운", "게으른", "여유로운", "소중한", "살찐", "만지면 물어요", "기분 좋은", "그게 나야", "힙합 전사", "Y2K", "흥부자", "나야나", "에너자이저", "끼쟁이", "너 T야?","I am", "뀨?"};
        String[] nickname2 =
                {"말레이시아곰", "티벳여우", "블롭피쉬", "골든햄스터", "썬더람쥐", "알각고래", "패럿", "뉴기니아앵무", "노루", "판다", "알파카", "기린", "바다표범", "가자미", "가오리", "물개", "카피바라", "거북이", "쿼카", "해달", "코브라", "치타", "표범", "나무늘보", "아르마딜로", "기니피그", "해파리", "긴날개여치", "왈라비", "사슴벌레", "개코도마뱀", "까마귀", "뻐꾸기", "슈가글라이더", "개미햝기", "벌꿀오소리", "칡...삵!"};

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
