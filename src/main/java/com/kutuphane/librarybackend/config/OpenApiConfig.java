package com.kutuphane.librarybackend.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name = "E-Kütüphane Yönetimi",
            email = "info@kutuphane.com",
            url = "https://kutuphane-projesi.com"
        ),
        description = "E-Kütüphane Projesi Backend API Dokümantasyonu (Spring Boot 3 + JWT)",
        title = "E-Kütüphane API",
        version = "1.0.0"
    ),
    security = {
        @SecurityRequirement(name = "bearerAuth") // Tüm endpoint'lerde kilit ikonunu aktif eder
    }
)
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT Token ile yetkilendirme (Örn: Bearer eyJhbG...)",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}