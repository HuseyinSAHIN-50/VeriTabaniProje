package com.kutuphane.akillikutuphane.controller;

import com.kutuphane.akillikutuphane.entity.Category;
import com.kutuphane.akillikutuphane.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController // 1. Bu sınıfın REST API isteklerini yöneteceğini belirtir.
@RequestMapping("/api/categories") // 2. Tüm metotlar için temel URL yolunu belirler.
public class CategoryController {

    private final CategoryService categoryService;

    // Service'i enjekte etme
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // ----------------------------------------------------
    // 1. POST: Yeni Kategori Oluşturma (Create)
    // URL: POST /api/categories
    // ----------------------------------------------------
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category savedCategory = categoryService.save(category);
        // 201 Created durum kodu ile yanıt döndürür
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    // ----------------------------------------------------
    // 2. GET: Tüm Kategorileri Listeleme (Read All)
    // URL: GET /api/categories
    // ----------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories); // 200 OK durum kodu
    }

    // ----------------------------------------------------
    // 3. GET: ID'ye Göre Kategori Getirme (Read One)
    // URL: GET /api/categories/{id}
    // ----------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.findById(id)
                .map(ResponseEntity::ok) // Bulunursa 200 OK ile döndür
                .orElseGet(() -> ResponseEntity.notFound().build()); // Bulunamazsa 404 Not Found döndür
    }

    // ----------------------------------------------------
    // 4. PUT: Kategori Güncelleme (Update)
    // URL: PUT /api/categories/{id}
    // ----------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
        return categoryService.findById(id)
                .map(existingCategory -> {
                    // Verileri güncelle
                    existingCategory.setName(categoryDetails.getName());
                    existingCategory.setDescription(categoryDetails.getDescription());
                    Category updatedCategory = categoryService.save(existingCategory);
                    return ResponseEntity.ok(updatedCategory);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ----------------------------------------------------
    // 5. DELETE: Kategori Silme (Delete)
    // URL: DELETE /api/categories/{id}
    // ----------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (categoryService.findById(id).isPresent()) {
            categoryService.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content döndür
        }
        return ResponseEntity.notFound().build(); // Bulunamazsa 404 Not Found döndür
    }
}