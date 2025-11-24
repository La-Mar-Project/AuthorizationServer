package com.jjubul.authserver.dto;

import com.jjubul.authserver.authorization.Provider;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidatedMyTokenDto {

    Provider provider;
    String sub;

    private ValidatedMyTokenDto(Provider provider, String sub) {
        this.provider = provider;
        this.sub = sub;
    }

    public static ValidatedMyTokenDto create(Provider provider, String sub) {
        return new ValidatedMyTokenDto(provider, sub);
    }
}
