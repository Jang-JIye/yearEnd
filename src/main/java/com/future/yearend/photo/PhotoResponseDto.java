package com.future.yearend.photo;

import lombok.Getter;

@Getter
public class PhotoResponseDto {
    private Long id;
    private final String photoURL;

    private final String month;


    public PhotoResponseDto(Photo photo) {
        this.id = photo.getId();
        this.photoURL = photo.getPhotoURL();
        this.month = photo.getMonth();
    }

    public PhotoResponseDto(String photoURL, String month) {
        this.photoURL = photoURL;
        this.month = month;
    }
}
