package com.jjubul.authserver.dto;

import lombok.Data;

@Data
public class NewLocalUserDto {

    private String account;
    private String password;
}
