package com.future.yearend.user;

import com.future.yearend.common.UserRoleEnum;
import com.future.yearend.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.channels.IllegalChannelGroupException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${ADMIN_TOKEN}")
    private String ADMIN_TOKEN;

    public ResponseEntity<String> signup(HttpServletResponse response, LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String phoneNum = loginRequestDto.getPhoneNum();
        UserRoleEnum userRole = UserRoleEnum.USER;

        if (loginRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
            userRole = UserRoleEnum.ADMIN;
        }
        Optional<User> existUser = userRepository.findByUsernameAndPhoneNum(username, phoneNum);
        if (existUser.isPresent()) {
            String token = jwtUtil.createToken(username, userRole, phoneNum);
            response.addHeader("Authorization", token);
        } else {
            // 회원가입
            User user = new User(username, phoneNum, userRole);
            userRepository.save(user);
            //로그인
            if (!loginRequestDto.getPhoneNum().equals(user.getPhoneNum())) {
                throw new IllegalArgumentException("핸드폰 뒷번호 4자리가 일치하지 않습니다.");
            }
            String token = jwtUtil.createToken(username, userRole,phoneNum);
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

    // 권한
    private void checkAuthority(User existUser, User user) {
        if (user.getUserRole().equals(UserRoleEnum.USER) && !existUser.getId().equals(user.getId())) {
            throw new IllegalArgumentException("사용자가 일치하지 않습니다.");
        }
    }
}
