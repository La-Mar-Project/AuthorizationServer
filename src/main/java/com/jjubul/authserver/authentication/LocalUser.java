package com.jjubul.authserver.authentication;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "local_users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocalUser {

    // 자체 id Token 에 담을 sub
    @Id @GeneratedValue
    private Long id;

    @Column(name = "local_user_account")
    private String account;

    @Column(name = "local_user_password")
    private String password;

    private LocalUser(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public static LocalUser create(String account, String password) {
        return new LocalUser(account, password);
    }


}
