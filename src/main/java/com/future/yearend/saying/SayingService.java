package com.future.yearend.saying;

import com.future.yearend.common.UserRoleEnum;
import com.future.yearend.user.User;
import com.future.yearend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SayingService {
    private final SayingRepository sayingRepository;
    private final UserRepository userRepository;

    public SayingResponseDto createSaying(SayingRequestDto sayingRequestDto, User user) {
        User findUser = findUser(user.getUsername());
        if (!findUser.getUserRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException("명언 등록 권한이 없습니다.");
        }
        Saying saying = new Saying(sayingRequestDto.getSaying(), sayingRequestDto.getSinger());
        Saying saveSaying = sayingRepository.save(saying);

        return new SayingResponseDto(saveSaying);
    }
    public ResponseEntity<SayingResponseDto> getRandomSaying() {
        Saying randomSaying = sayingRepository.findRandomSaying();
        SayingResponseDto sayingResponseDto = new SayingResponseDto(randomSaying);

        return ResponseEntity.ok(sayingResponseDto);
    }

    public ResponseEntity<String> deleteSaying(Long id, User user) {
        User findUser = findUser(user.getUsername());
        if (!findUser.getUserRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException("명언 삭제 권한이 없습니다.");
        }
        Saying findSaying = sayingRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 명언입니다.")
        );
        sayingRepository.delete(findSaying);
        return ResponseEntity.ok("삭제 완료!");
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                ()-> new IllegalArgumentException("해당 작성자는 존재하지 않습니다.")
        );
    }
}
