package com.jjubul.authserver.service;

import com.jjubul.authserver.authentication.LocalUser;
import com.jjubul.authserver.repository.LocalUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LocalUserService {

    private final PasswordEncoder passwordEncoder;
    private final LocalUserRepository localUserRepository;

    public LocalUser newUser(String account, String password) {

        if (localUserRepository.existsByAccount(account)) {
            throw new IllegalStateException("Account already exists");
        }

        LocalUser localUser = LocalUser.create(account, passwordEncoder.encode(password));

        return localUserRepository.save(localUser);
    }
}
