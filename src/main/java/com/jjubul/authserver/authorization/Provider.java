package com.jjubul.authserver.authorization;

public enum Provider {
    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao"),
    LOCAL("local");

    private final String value;

    Provider(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Provider from(String value) {
        for (Provider provider : Provider.values()) {
            if (provider.value.equalsIgnoreCase(value)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("Unknown provider: " + value);
    }




}
