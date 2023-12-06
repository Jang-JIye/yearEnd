package com.future.yearend.entity;

import com.future.yearend.common.TimeStamped;
import com.future.yearend.dto.MemoRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "memo")
public class Memo extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "username", nullable = true)
//    private String username;
//
//    @Column(name = "phoneNumber", nullable = false)
//    private String phoneNum;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "date", nullable = false)
    private String date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Memo(MemoRequestDto memoRequestDto, User user) {
//        this.username = user.getUsername();
//        this.phoneNum = user.getPhoneNum();
        this.nickname = memoRequestDto.getNickname();
        this.contents = memoRequestDto.getContents();
        this.date = memoRequestDto.getDate();
        this.user = user;
    }

    public void update(MemoRequestDto memoRequestDto, User user) {
//        this.username = user.getUsername();
//        this.phoneNum = user.getPhoneNum();
        this.nickname = memoRequestDto.getNickname();
        this.contents = memoRequestDto.getContents();
        this.date = memoRequestDto.getDate();
        this.user = user;
    }
}
