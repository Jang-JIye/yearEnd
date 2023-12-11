package com.future.yearend.photo;

import lombok.Getter;

@Getter
public class S3ResponseDto {
    private Long photoId;
    private String photoURL;
    private String photoName;
    private String photoContentType;

    public S3ResponseDto(Photo photo) {
        this.photoId = photo.getId();
    }
}
