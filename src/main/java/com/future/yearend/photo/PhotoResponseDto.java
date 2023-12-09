package com.future.yearend.photo;

import lombok.Getter;

@Getter
public class PhotoResponseDto {
    private Long id;
    private String photoURL;


    public PhotoResponseDto(Photo photo) {
        this.id = photo.getId();
        this.photoURL = photo.getPhotoURL();
    }
}
