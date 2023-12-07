package com.future.yearend.user;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/signup")
    public ResponseEntity<String> signup(HttpServletResponse response, @RequestBody LoginRequestDto loginRequestDto) {
        return userService.signup(response, loginRequestDto);
    }
}
