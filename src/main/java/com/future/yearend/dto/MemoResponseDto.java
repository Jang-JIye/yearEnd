package com.future.yearend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.yearend.entity.Memo;
import lombok.Getter;

@Getter
public class MemoResponseDto {
    @JsonIgnore
    private String username;
    @JsonIgnore
    private String phoneNum;
    private String nickname;
    private String contents;
    private String date;


    public MemoResponseDto(Memo memo) {
        this.username = memo.getUsername();
        this.phoneNum = memo.getPhoneNum();
        this.nickname = memo.getNickname();
        this.contents = memo.getContents();
        this.date = memo.getDate();
    }
}
