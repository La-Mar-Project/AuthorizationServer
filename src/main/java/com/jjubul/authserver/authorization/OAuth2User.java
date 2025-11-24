package com.jjubul.authserver.authorization;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "oauth2_users",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"oauth2_user_provider", "oauth2_user_sub"})}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oauth2_user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "oauth2_user_provider")
    private Provider provider;

    @Column(name = "oauth2_user_sub")
    private String sub;


    @Enumerated(EnumType.STRING)
    @Column(name = "user_grade")
    private Grade grade;

    @Column(name = "user_name")
    private String name;

    @Builder
    private OAuth2User(Provider provider, String sub, Grade grade, String name) {
        this.provider = provider;
        this.sub = sub;
        this.grade = grade;
        this.name = name;
    }

    public static OAuth2User create(Provider provider, String sub, String name) {
        return OAuth2User.builder()
                .provider(provider)
                .sub(sub)
                .grade(Grade.BASIC)
                .name(name)
                .build();
    }
}
