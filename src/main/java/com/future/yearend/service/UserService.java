package com.future.yearend.service;

import com.future.yearend.common.JwtUtil;
import com.future.yearend.dto.LoginRequestDto;
import com.future.yearend.entity.User;
import com.future.yearend.repository.UserRepository;
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
}
