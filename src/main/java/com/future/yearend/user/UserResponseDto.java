package com.future.yearend.user;

import com.future.yearend.memo.Memo;
import com.future.yearend.memo.MemoResponseDto;
import com.future.yearend.photo.Photo;
import com.future.yearend.photo.PhotoResponseDto;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserResponseDto {
    private final String username;
    private final String phoneNum;

    public UserResponseDto(User user) {
        this.username = user.getUsername();
        this.phoneNum = user.getPhoneNum();
    }
}
