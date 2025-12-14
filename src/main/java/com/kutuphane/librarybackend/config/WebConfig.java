package com.kutuphane.librarybackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // "/uploads/**" adresine gelen istekleri, projenin kökündeki "uploads" klasörüne yönlendir
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}