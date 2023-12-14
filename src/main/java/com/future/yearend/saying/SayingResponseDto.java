package com.future.yearend.saying;

import lombok.Getter;

@Getter
public class SayingResponseDto {
    private final Long id;
    private final String saying;
    private final String singer;
    public SayingResponseDto(Saying saying) {
        this.id = saying.getId();
        this.saying = saying.getSaying();
        this.singer = saying.getSinger();
    }
}
