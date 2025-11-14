package com.jjubul.authserver.service;

import com.jjubul.authserver.authentication.LocalUser;
import com.jjubul.authserver.repository.LocalUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LocalUserService {

    private final LocalUserRepository localUserRepository;

    public LocalUser getUser(String account) {
        return localUserRepository.findLocalUserByAccount(account).orElseThrow(EntityNotFoundException::new);
    }

    public LocalUser newUser(String account, String password) {
        return localUserRepository.save(new LocalUser(account, password));
    }
}
