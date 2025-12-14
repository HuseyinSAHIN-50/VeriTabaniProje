package com.kutuphane.librarybackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kutuphane.librarybackend.dto.LoginRequest;
import com.kutuphane.librarybackend.dto.NewPasswordRequest;
import com.kutuphane.librarybackend.dto.PasswordResetRequest;
import com.kutuphane.librarybackend.dto.RegisterRequest;
import com.kutuphane.librarybackend.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }


@PostMapping("/login")
public ResponseEntity<String> login(@RequestBody LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
}
@PostMapping("/forgot-password")
public ResponseEntity<String> forgotPassword(@RequestBody PasswordResetRequest request) {
    authService.forgotPassword(request.getEmail());
    return ResponseEntity.ok("Sıfırlama kodu e-posta adresinize gönderildi.");
}

@PostMapping("/reset-password")
public ResponseEntity<String> resetPassword(@RequestBody NewPasswordRequest request) {
    authService.resetPassword(request.getEmail(), request.getCode(), request.getNewPassword());
    return ResponseEntity.ok("Şifreniz başarıyla değiştirildi. Giriş yapabilirsiniz.");
}
}