package com.future.yearend.user;

import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String username;
    private String phoneNum;

    private boolean admin = false;
    private String adminToken = "";
}
