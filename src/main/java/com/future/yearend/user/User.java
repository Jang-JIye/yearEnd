package com.future.yearend.user;

import com.future.yearend.common.UserRoleEnum;
import com.future.yearend.memo.Memo;
import com.future.yearend.photo.Photo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String phoneNum;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum userRole;


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Memo> memoList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Photo> photoList = new ArrayList<>();

    public User(String username, String phoneNum, UserRoleEnum userRole) {
        this.username = username;
        this.phoneNum = phoneNum;
        this.userRole = userRole;
    }
}
