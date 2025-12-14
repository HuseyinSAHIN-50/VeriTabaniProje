package com.kutuphane.librarybackend.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kutuphane.librarybackend.dto.LoginRequest;
import com.kutuphane.librarybackend.dto.RegisterRequest;
import com.kutuphane.librarybackend.entity.Role;
import com.kutuphane.librarybackend.entity.User;
import com.kutuphane.librarybackend.repository.RoleRepository;
import com.kutuphane.librarybackend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    public String register(RegisterRequest request) {
        // 1. Email kontrolü
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Bu email adresi zaten kullanılıyor.");
        }

        // 2. Varsayılan Rolü Bul (ROLE_USER)
        Role userRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Rol bulunamadı. Veritabanı scriptlerini çalıştırdınız mı?"));

        // 3. Kullanıcıyı Oluştur ve Şifreyi Hashle
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword())) // Şifreyi şifrele!
                .role(userRole)
                .build();

        // 4. Kaydet
        userRepository.save(user);

        return "Kullanıcı başarıyla kaydedildi.";
    }
    public String login(LoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );

    var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
    
    var userDetails = org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .roles(user.getRole().getRoleName().replace("ROLE_", ""))
            .build();

    // --- YENİ EKLENEN KISIM BAŞLANGIÇ ---
    // Token'ın içine Rol bilgisini (örn: ROLE_ADMIN) gizliyoruz
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("role", user.getRole().getRoleName());

    // Token üretirken bu bilgiyi de gönderiyoruz
    return jwtService.generateToken(extraClaims, userDetails);
    // --- YENİ EKLENEN KISIM BİTİŞ ---
}
// 1. ŞİFRE SIFIRLAMA KODU GÖNDER
public void forgotPassword(String email) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));

    // 6 Haneli Rastgele Kod Üret
    String code = String.valueOf(new Random().nextInt(900000) + 100000);

    // Veritabanına kaydet (Süre: 5 dakika)
    user.setResetCode(code);
    user.setResetExpiration(LocalDateTime.now().plusMinutes(5));
    userRepository.save(user);

    // Mail Gönder
    emailService.sendSimpleEmail(
        email, 
        "Şifre Sıfırlama Kodu", 
        "Şifre sıfırlama kodunuz: " + code + "\nBu kod 5 dakika geçerlidir."
    );
}

// 2. KOD İLE YENİ ŞİFRE BELİRLE
public void resetPassword(String email, String code, String newPassword) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));

    // Kod Kontrolü
    if (user.getResetCode() == null || !user.getResetCode().equals(code)) {
        throw new RuntimeException("Geçersiz kod!");
    }

    // Süre Kontrolü
    if (user.getResetExpiration().isBefore(LocalDateTime.now())) {
        throw new RuntimeException("Kodun süresi dolmuş. Tekrar deneyin.");
    }

    // Yeni Şifreyi Kaydet
    user.setPassword(passwordEncoder.encode(newPassword));
    user.setResetCode(null); // Kodu temizle (tek kullanımlık olsun)
    user.setResetExpiration(null);
    userRepository.save(user);
}
}