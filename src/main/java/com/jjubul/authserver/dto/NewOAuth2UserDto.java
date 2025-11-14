package com.jjubul.authserver.dto;

import com.jjubul.authserver.authorization.Grade;
import com.jjubul.authserver.authorization.Provider;
import lombok.Data;

@Data
public class NewOAuth2UserDto {
    private Provider provider;
    private String sub;
    private String email;
    private Grade grade;
    private String name;
}
