package com.kutuphane.akillikutuphane.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}") 
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;

    // ----------------------------------------------------
    // 1. JWT Token Üretme
    // ----------------------------------------------------
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationInMs);

        // Gizli anahtarı güvenli bir şekilde Key nesnesine dönüştürme
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        
        String token = Jwts.builder()
                .setSubject(username) 
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key) 
                .compact();
                
        return token;
    }

    // ----------------------------------------------------
    // 2. Token'dan Kullanıcı Adını (Email) Alma
    // ----------------------------------------------------
    public String getUsernameFromJWT(String token) {
        // Token'ı ayrıştır ve Subject (bizim e-posta adresimiz) alanını al
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ----------------------------------------------------
    // 3. Token'ı Doğrulama
    // ----------------------------------------------------
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            // Loglama: Geçersiz JWT imzası
        } catch (MalformedJwtException ex) {
            // Loglama: Hatalı biçimlendirilmiş JWT
        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            // Loglama: JWT süresi dolmuş
        } catch (UnsupportedJwtException ex) {
            // Loglama: Desteklenmeyen JWT
        } catch (IllegalArgumentException ex) {
            // Loglama: JWT claim string'i boş
        }
        return false;
    }
}