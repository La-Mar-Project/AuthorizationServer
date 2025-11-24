package com.jjubul.authserver.dto;

import com.jjubul.authserver.authorization.Provider;
import lombok.Data;

@Data
public class NewUserDto {
    private String jwt;
    private String username;
    private String nickname;
    private String phone;
}
