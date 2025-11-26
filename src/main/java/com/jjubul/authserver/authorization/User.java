package com.jjubul.authserver.authorization;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_sub")
    private String sub;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_provider")
    private Provider provider;

    @Column(name = "user_username")
    private String username;

    @Column(name = "user_nickname", unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_grade")
    private Grade grade;

    @Column(name = "user_phone", unique = true)
    private String phone;

    @Builder
    private User(String username, String nickname, Grade grade, String phone, String sub, Provider provider) {
        this.username = username;
        this.nickname = nickname;
        this.grade = grade;
        this.phone = phone;
        this.sub = sub;
        this.provider = provider;
    }

    public static User create(String username, String nickname, Grade grade, String phone, String sub, Provider provider) {
        return User.builder()
                .username(username)
                .nickname(nickname)
                .grade(grade)
                .phone(phone)
                .sub(sub)
                .provider(provider)
                .build();
    }
}
