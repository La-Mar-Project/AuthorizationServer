package com.jjubul.authserver.repository;

import com.jjubul.authserver.authorization.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByValue(String value);
    void deleteByValue(String value);
}
