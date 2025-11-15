package com.jjubul.authserver.service;

import com.jjubul.authserver.authentication.LocalUser;
import com.jjubul.authserver.repository.LocalUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final LocalUserRepository localUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LocalUser user = localUserRepository.findLocalUserByAccount(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return User.withUsername(user.getAccount())
                .password(user.getPassword())
                .authorities("ROLE_USER")
                .build();
    }


}
