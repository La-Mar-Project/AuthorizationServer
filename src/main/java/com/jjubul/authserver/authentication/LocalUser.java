package com.jjubul.authserver.authentication;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "local_users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocalUser {

    // 자체 id Token 에 담을 sub
    @Id @GeneratedValue
    private Long id;

    @Column(name = "local_user_account")
    private String account;

    @Column(name = "local_user_password")
    private String password;

    public LocalUser(String account, String password) {
        this.account = account;
        this.password = password;
    }
}
