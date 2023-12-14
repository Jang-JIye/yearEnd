package com.future.yearend.user;


import com.future.yearend.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/api/user")
    @Operation(summary = "사용자 조회(본인)", description = "사용자 조회(본인) API 입니다.")
    public ResponseEntity<UserResponseDto> getUsernameAndPhoneNum(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getUsernameAndPhoneNum(userDetails.getUser());
    }

    //------------- admin ----------------
    @GetMapping("/api/admin/username/{username}")
    @Operation(summary = "어드민 사용자 조회", description = "사용자 이름으로 조회하는 API 입니다.")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getUserByUsername(username, userDetails.getUser());
    }

    @GetMapping("/api/admin/nickname/{nickname}")
    @Operation(summary = "어드민 사용자 닉네임 조회", description = "사용자 닉네임으로 조회하는 API 입니다.")
    public ResponseEntity<UserResponseDto> getUserByNickname(@PathVariable String nickname,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getUserByNickname(nickname, userDetails.getUser());
    }


}
