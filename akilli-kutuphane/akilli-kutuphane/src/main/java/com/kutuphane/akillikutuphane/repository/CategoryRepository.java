package com.kutuphane.akillikutuphane.repository; // Kendi paket adınızı kontrol edin!

import org.springframework.data.jpa.repository.JpaRepository;

import com.kutuphane.akillikutuphane.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}