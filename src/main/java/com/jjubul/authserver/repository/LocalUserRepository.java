package com.jjubul.authserver.repository;

import com.jjubul.authserver.authentication.LocalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocalUserRepository extends JpaRepository<LocalUser, Long> {


    Optional<LocalUser> findLocalUserByAccount(String account);

    boolean existsByAccount(String account);

    Optional<LocalUser> findLocalUserByAccountAndPassword(String account, String password);
}
