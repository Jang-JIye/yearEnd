package com.future.yearend.entity;

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


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Memo> memoList = new ArrayList<>();

    public User(String username, String phoneNum) {
        this.username = username;
        this.phoneNum = phoneNum;
    }
}
