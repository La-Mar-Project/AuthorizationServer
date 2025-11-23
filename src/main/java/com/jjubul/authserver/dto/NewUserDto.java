package com.jjubul.authserver.dto;

import com.jjubul.authserver.authorization.Provider;
import lombok.Data;

@Data
public class NewUserDto {
    private Provider provider;
    private String sub;
    private String username;
    private String nickname;
    private String phone;
}
