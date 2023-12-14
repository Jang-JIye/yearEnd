package com.future.yearend.user;

import com.future.yearend.memo.Memo;
import com.future.yearend.memo.MemoResponseDto;
import com.future.yearend.photo.Photo;
import com.future.yearend.photo.PhotoResponseDto;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserResponseDto {
    private final String username;
    private final String phoneNum;
    private List<MemoResponseDto> memoList;
    private List<PhotoResponseDto> photoList;

    public UserResponseDto(User user) {
        this.username = user.getUsername();
        this.phoneNum = user.getPhoneNum();
    }
    public UserResponseDto(User user, List<Memo> memos, List<Photo> photos) {
        this.username = user.getUsername();
        this.phoneNum = user.getPhoneNum();
        this.memoList = memos.stream()
                .map(MemoResponseDto::new)
                .collect(Collectors.toList());
        this.photoList = photos.stream()
                .map(PhotoResponseDto::new)
                .collect(Collectors.toList());
    }
}
