package com.kutuphane.akillikutuphane.service;

import com.kutuphane.akillikutuphane.entity.Category;
import com.kutuphane.akillikutuphane.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service // 1. Bu sınıfın Spring tarafından yönetilen bir servis bileşeni olduğunu belirtir.
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Constructor Injection: Spring, CategoryRepository'nin bir örneğini otomatik olarak sağlar.
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // ----------------------------------------------------
    // CRUD İşlemleri için Temel Metotlar
    // ----------------------------------------------------

    // 1. OLUŞTURMA (Create) ve GÜNCELLEME (Update)
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    // 2. OKUMA (Read) - Tekil
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    // 2. OKUMA (Read) - Hepsi
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    // 3. SİLME (Delete)
    public void deleteById(Long id) {
        // İŞ KURALI ÖRNEĞİ: Silmeden önce kategoriye ait kitap var mı kontrolü buraya eklenebilir.
        categoryRepository.deleteById(id);
    }

    // Özel Repository metodunu kullanma
    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }
}