package com.kutuphane.akillikutuphane.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder; // YENİ IMPORT
import org.springframework.stereotype.Service;

import com.kutuphane.akillikutuphane.entity.User;
import com.kutuphane.akillikutuphane.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // YENİ ALAN (Şifreleyici)

    // Constructor Injection'ı güncellendi: PasswordEncoder enjekte ediliyor
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; 
    }

    // save metotunu güncellendi: Şifre kaydetmeden önce hashlenir
    public User save(User user) {
        // Gelen düz metin şifreyi (user.getPasswordHash()) BCrypt ile hash'le
        String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
        // Hashlenmiş şifreyi tekrar User nesnesine set et
        user.setPasswordHash(hashedPassword);
        
        return userRepository.save(user);
    }

    // ----------------------------------------------------
    // Diğer Metotlar (Mevcut CRUD işlevleri)
    // ----------------------------------------------------
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}