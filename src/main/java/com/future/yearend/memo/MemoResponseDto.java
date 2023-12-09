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
    private Long id;
    private String nickname;
    private String contents;
    //    private String date;
    private String month;
    private String day;


    public MemoResponseDto(Memo memo, User user) {
        this.username = memo.getUser().getUsername();
        this.phoneNum = memo.getUser().getPhoneNum();
        this.id = memo.getId();
        this.nickname = memo.getNickname();
        this.contents = memo.getContents();
//        this.date = memo.getDate();
        this.month = memo.getMonth();
        this.day = memo.getDay();
    }
    public MemoResponseDto(Memo memo) {
        this.username = memo.getUser().getUsername();
        this.phoneNum = memo.getUser().getPhoneNum();
        this.id = memo.getId();
        this.nickname = memo.getNickname();
        this.contents = memo.getContents();
        this.month = memo.getMonth();
        this.day = memo.getDay();
    }
}
