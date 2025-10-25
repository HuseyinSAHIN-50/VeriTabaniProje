package com.kutuphane.akillikutuphane.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kutuphane.akillikutuphane.entity.User;
import com.kutuphane.akillikutuphane.service.UserService;

@RestController
@RequestMapping("/api/users") // Bu Controller'ın temel URL yolu
public class UserController {

    private final UserService userService;

    // Service'i enjekte etme
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ----------------------------------------------------
    // 1. POST: Yeni Kullanıcı Kaydı (Register/Create)
    // NOT: İleride şifre hashleme burada yapılacak.
    // URL: POST /api/users
    // ----------------------------------------------------
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // İŞ KURALI NOTU: İleride bu API sadece ADMIN tarafından kullanılabilir veya
        // '/api/auth/register' gibi ayrı bir uç noktaya taşınabilir.
        User savedUser = userService.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED); // 201 Created
    }

    // ----------------------------------------------------
    // 2. GET: Tüm Kullanıcıları Listeleme (Read All)
    // NOT: İleride bu API sadece ADMIN tarafından erişilebilir olacaktır.
    // URL: GET /api/users
    // ----------------------------------------------------
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users); // 200 OK
    }

    // ----------------------------------------------------
    // 3. GET: ID'ye Göre Kullanıcı Getirme (Read One)
    // URL: GET /api/users/{id}
    // ----------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404 Not Found
    }

    // ----------------------------------------------------
    // 4. DELETE: Kullanıcı Silme (Delete)
    // NOT: İleride bu API sadece ADMIN tarafından erişilebilir olacaktır.
    // URL: DELETE /api/users/{id}
    // ----------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.findById(id).isPresent()) {
            userService.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build(); // 404 Not Found
    }

    // NOT: PUT (Güncelleme) operasyonu, kullanıcıların kendi profillerini güncellemeleri
    // gerektiğinde veya admin yetkisiyle başka bir kullanıcıyı güncellerken eklenecektir.
}