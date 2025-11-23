package com.jjubul.authserver.authorization;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_sub")
    private String sub;

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

    private User(String username, String nickname, Grade grade, String phone, String sub, Provider provider) {
        this.username = username;
        this.nickname = nickname;
        this.grade = grade;
        this.phone = phone;
        this.sub = sub;
        this.provider = provider;
    }

    public static User create(String username, String nickname, Grade grade, String phone, String sub, Provider provider) {
        return new User(username, nickname, grade, phone, sub, provider);
    }
}
