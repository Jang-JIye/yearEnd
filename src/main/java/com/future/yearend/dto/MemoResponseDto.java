package com.future.yearend.dto;

import com.future.yearend.Memo.Memo;
import lombok.Getter;

@Getter
public class MemoResponseDto {
    private String title;
    private String username;
    private String contents;
    private String date;


    public MemoResponseDto(Memo memo) {
        this.title = memo.getTitle();
        this.username = memo.getUsername();
        this.contents = memo.getContents();
        this.date = memo.getDate();
    }
}
