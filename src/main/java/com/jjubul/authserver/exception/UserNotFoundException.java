package com.jjubul.authserver.exception;

import com.jjubul.authserver.authorization.Provider;
import com.jjubul.authserver.authorization.User;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {

    private Provider provider;
    private String sub;

    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(Provider provider, String sub) {
        super("User not found");
        this.provider = provider;
        this.sub = sub;
    }
}
