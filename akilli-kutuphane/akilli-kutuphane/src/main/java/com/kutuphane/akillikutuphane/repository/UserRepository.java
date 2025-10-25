package com.kutuphane.akillikutuphane.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kutuphane.akillikutuphane.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // Kimlik doğrulama için gerekli özel metot
    Optional<User> findByEmail(String email);
}