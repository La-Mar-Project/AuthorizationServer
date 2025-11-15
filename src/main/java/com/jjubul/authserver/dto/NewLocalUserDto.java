package com.jjubul.authserver.dto;

import com.jjubul.authserver.authorization.Grade;
import lombok.Data;

@Data
public class NewLocalUserDto {

    private String account;
    private String password;
    private String email;
    private String name;
}
