package com.future.yearend.saying;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "sayings")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Saying {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "saying", nullable = false)
    private String saying;

    @Column(name = "singer", nullable = false)
    private String singer;

    public Saying(String saying, String singer) {
        this.saying = saying;
        this.singer = singer;
    }
}
