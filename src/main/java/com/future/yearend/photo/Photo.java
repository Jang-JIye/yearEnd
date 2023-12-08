package com.future.yearend.photo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.future.yearend.common.TimeStamped;
import com.future.yearend.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "photo")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Photo extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    @JsonIgnore
    private String username;

    @Column(name = "phoneNumber", nullable = false)
    @JsonIgnore
    private String phoneNum;

    @Column(nullable = false)
    private String photoURL;

    @Column(nullable = true)
    private String photoName;

    @Column(nullable = true)
    private String photoContentType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Photo(String photoURL, String photoName, String photoContentType, User user) {
        this.photoURL = photoURL;
        this.photoName = photoName;
        this.photoContentType = photoContentType;
        this.user = user;
        this.username = user.getUsername();
        this.phoneNum = user.getPhoneNum();
    }
}
