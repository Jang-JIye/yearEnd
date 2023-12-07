package com.future.yearend.photo;

import lombok.Getter;

@Getter
public class S3ResponseDto {
    private Long photoId;
    private String photoURL;
    private String picturesName;
    private String pictureContentType;

    public S3ResponseDto(Photo photo) {
        this.photoId = photo.getId();
    }
}
