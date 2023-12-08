package com.future.yearend.user;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "사용자 관련 API", description = "사용자 관련 API")
public class UserController {
    private final UserService userService;

    @PostMapping("/api/signup")
    @Operation(summary = "회원가입, 로그인", description = "회원가입, 로그인 API 입니다.")
    public ResponseEntity<String> signup(HttpServletResponse response, @RequestBody LoginRequestDto loginRequestDto) {
        return userService.signup(response, loginRequestDto);
    }
}
