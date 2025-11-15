package com.jjubul.authserver.repository;

import com.jjubul.authserver.authorization.OAuth2User;
import com.jjubul.authserver.authorization.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OAuth2UserRepository extends JpaRepository<OAuth2User, Long> {
     Optional<OAuth2User> findOAuth2UserByProviderAndSub(Provider provider, String sub);

     Boolean existsByProviderAndSub(Provider provider, String sub);
}
