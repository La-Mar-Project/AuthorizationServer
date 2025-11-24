package com.jjubul.authserver.exception;

import com.jjubul.authserver.authorization.Provider;
import com.jjubul.authserver.authorization.User;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {

    private final Provider provider;
    private final String sub;

    public UserNotFoundException(Provider provider, String sub) {
        super("User not found");
        this.provider = provider;
        this.sub = sub;
    }
}
