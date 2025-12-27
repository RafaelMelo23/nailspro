package com.rafael.nailspro.webapp.model.repository;

import com.rafael.nailspro.webapp.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByEmail(String email);

    Optional<User> findByEmailIgnoreCase(String email);

    @Modifying
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.email = :email")
    void updatePassword(@Param("email") String userEmail,
                        @Param("newPassword") String newPassword);

    Long getUser_IdByEmail(String email);

    boolean existsByEmail(String email);

}
