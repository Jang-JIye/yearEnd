package com.future.yearend.memo;

import lombok.Getter;

@Getter
public class MemoRequestDto {

    private String username;
    private String phoneNum;
    private String nickname;
    private String contents;
    private String date;
}
