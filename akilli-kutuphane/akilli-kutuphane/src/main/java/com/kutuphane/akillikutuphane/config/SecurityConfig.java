package com.kutuphane.akillikutuphane.config;

import org.springframework.context.annotation.Bean; // YENİ IMPORT: Kendi yazdığımız filtre
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // YENİ IMPORT: STATELESS oturum için
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kutuphane.akillikutuphane.security.JwtAuthenticationFilter; // YENİ IMPORT

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter authenticationFilter;
    
    // Constructor Injection: JwtAuthenticationFilter'ı enjekte ediyoruz
    public SecurityConfig(JwtAuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    // 1. Şifreleri hashlemek için PasswordEncoder Bean'i
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // 2. AuthenticationManager Bean'i (JWT Giriş için gerekli)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // 3. Güvenlik Filtre Zinciri (API'leri korur ve yetkilendirme uygular)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF devre dışı
            .sessionManagement(session -> session
                // JWT kullandığımız için oturum tutmuyoruz (STATELESS)
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Token Alımı (Login) herkes için AÇIK
                .requestMatchers("/api/auth/**").permitAll()
                
                // ADMIN İşlemleri: Kitap, Yazar ve Kategori CRUD'u ADMIN'e kısıtla
                // Proje gereksinimi: Admin yetkisine sahip kullanıcılar kitap/yazar/kategori ekleyebilir/silebilir
                .requestMatchers("/api/books/**", "/api/categories/**", "/api/authors/**").hasAuthority("ADMIN")
                
                // Kullanıcı API'leri: Kullanıcıları listeleme/silme ADMIN yetkisi olmalı.
                .requestMatchers("/api/users/**").hasAuthority("ADMIN")
                
                // Diğer tüm API'ler (Ödünç alma/iade, arama) GİRİŞ yapmış kullanıcılar için açık
                .anyRequest().authenticated()
            );

        // Kendi JWT filtremizi, Spring Security'nin varsayılan kimlik doğrulama filtrelerinden önce ekliyoruz
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}