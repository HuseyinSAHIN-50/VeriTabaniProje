package com.kutuphane.akillikutuphane.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // İleride oluşturacağız
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kutuphane.akillikutuphane.controller.payload.LoginDto;
import com.kutuphane.akillikutuphane.security.JwtTokenProvider;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    // Constructor Injection
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    // ----------------------------------------------------
    // POST: Kullanıcı Girişi (Login)
    // URL: POST /api/auth/login
    // ----------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto) {
        
        // 1. Kimlik Doğrulama
        // AuthenticationManager, UserDetailsService ve PasswordEncoder'ı kullanarak
        // kullanıcı adını (email) ve şifreyi doğrular.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()));

        // 2. Güvenlik Bağlamını Güncelle
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. JWT Token Üretme
        String token = tokenProvider.generateToken(authentication);

        // 4. Token'ı yanıt olarak gönder
        return new ResponseEntity<>("Bearer " + token, HttpStatus.OK);
    }
}