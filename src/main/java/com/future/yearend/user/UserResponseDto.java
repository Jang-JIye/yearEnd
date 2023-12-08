package com.future.yearend.user;

import lombok.Getter;

@Getter
public class UserResponseDto {
    private String username;
    private String phoneNum;
    public UserResponseDto(User user) {
        this.username = user.getUsername();
        this.phoneNum = user.getPhoneNum();
    }
}
