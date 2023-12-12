package com.future.yearend.memo;

import com.future.yearend.common.TimeStamped;
import com.future.yearend.user.User;
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

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "phoneNumber", nullable = false)
    private String phoneNum;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "contents", nullable = false)
    private String contents;

    //    @Column(name = "date", nullable = false)
//    private String date;
    @Column(name = "month", nullable = false)
    private String month;

    @Column(name = "day", nullable = false)
    private String day;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Memo(MemoRequestDto memoRequestDto, User user) {
        this.username = user.getUsername();
        this.phoneNum = user.getPhoneNum();
        this.nickname = memoRequestDto.getNickname();
        this.contents = memoRequestDto.getContents();
//        this.date = memoRequestDto.getDate();
        this.month = memoRequestDto.getMonth();
        this.day = memoRequestDto.getDay();
        this.user = user;
    }

    public Memo(String username,String phoneNum,String nickname,String contents, String month, String day, User user) {
        this.username = user.getUsername();
        this.phoneNum = user.getPhoneNum();
        this.nickname = nickname;
        this.contents = contents;
        this.month = month;
        this.day = day;
        this.user = user;
    }

    public void update(MemoRequestDto memoRequestDto, User user) {
        this.username = user.getUsername();
        this.phoneNum = user.getPhoneNum();
        this.nickname = memoRequestDto.getNickname();
        this.contents = memoRequestDto.getContents();
        this.month = memoRequestDto.getMonth();
        this.day = memoRequestDto.getDay();
        this.user = user;
    }
}
