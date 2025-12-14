package com.rafael.nailspro.webapp.model.repository;

import com.rafael.nailspro.webapp.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByEmail(String email);

    Optional<User> findByEmailIgnoreCase(String email);
}
