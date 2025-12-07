package com.akillikutuphane.akillikutuphane.controller;

import com.akillikutuphane.akillikutuphane.entity.User;
import com.akillikutuphane.akillikutuphane.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    // Constructor injection
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Tüm kullanıcıları listele
    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }

    // ID'ye göre kullanıcı getir
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Yeni kullanıcı ekle
    @PostMapping
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    // Kullanıcı güncelle
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        return userRepository.findById(id)
                .map(existing -> {
                    existing.setUsername(user.getUsername());
                    existing.setEmail(user.getEmail());
                    User updated = userRepository.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Kullanıcı sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
