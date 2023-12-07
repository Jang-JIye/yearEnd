package com.future.yearend.memo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.yearend.user.User;
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


    public MemoResponseDto(Memo memo, User user) {
        this.username = memo.getUser().getUsername();
        this.phoneNum = memo.getUser().getPhoneNum();
        this.nickname = memo.getNickname();
        this.contents = memo.getContents();
        this.date = memo.getDate();
    }
    public MemoResponseDto(Memo memo) {
        this.username = memo.getUser().getUsername();
        this.phoneNum = memo.getUser().getPhoneNum();
        this.nickname = memo.getNickname();
        this.contents = memo.getContents();
        this.date = memo.getDate();
    }
}
