package com.jjubul.authserver.repository;

import com.jjubul.authserver.authorization.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
