package com.jjubul.authserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
