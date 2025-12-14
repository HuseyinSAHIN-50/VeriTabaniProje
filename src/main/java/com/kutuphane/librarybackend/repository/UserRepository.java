package com.kutuphane.librarybackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kutuphane.librarybackend.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    // Login işlemi için email ile kullanıcı bulma
    Optional<User> findByEmail(String email);
    
    // Email daha önce alınmış mı kontrolü için
    boolean existsByEmail(String email);
}