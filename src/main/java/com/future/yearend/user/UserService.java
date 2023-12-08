package com.future.yearend.user;

import com.future.yearend.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    public ResponseEntity<String> signup(HttpServletResponse response, LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String phoneNum = loginRequestDto.getPhoneNum();

        Optional<User> existUser = userRepository.findByUsernameAndPhoneNum(username, phoneNum);
        if (existUser.isPresent()) {
            String token = jwtUtil.createToken(username, phoneNum);
            response.addHeader("Authorization", token);
        } else {
            // 회원가입
            User user = new User(username, phoneNum);
            userRepository.save(user);
            //로그인
            String token = jwtUtil.createToken(username, phoneNum);
            response.addHeader("Authorization", token);
        }
        return ResponseEntity.status(HttpStatus.OK).body("반가워요!");
    }

    public ResponseEntity<UserResponseDto> getUsernameAndPhoneNum(User user) {
        User existUser = checkUser(user); // 유저 확인
        checkAuthority(existUser, user); //권한 확인

        UserResponseDto userResponseDto = new UserResponseDto(existUser);
        return ResponseEntity.ok(userResponseDto);
    }

    // 사용자 확인 메서드
    private User checkUser(User user) {
        return userRepository.findById(user.getId()).
                orElseThrow(() -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다."));
    }

    // ADMIN 권한 및 이메일 일치여부 메서드
    private void checkAuthority(User existUser, User users) {
        if (!existUser.getId().equals(users.getId())) {
            throw new IllegalArgumentException("사용자가 일치하지 않습니다.");
        }
    }
}
