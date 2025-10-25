package com.kutuphane.akillikutuphane.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kutuphane.akillikutuphane.entity.Author;
import com.kutuphane.akillikutuphane.service.AuthorService;

@RestController
@RequestMapping("/api/authors") // Bu Controller'ın temel URL yolu
public class AuthorController {

    private final AuthorService authorService;

    // Service'i enjekte etme (Constructor Injection)
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // ----------------------------------------------------
    // 1. POST: Yeni Yazar Oluşturma (Create)
    // URL: POST /api/authors
    // ----------------------------------------------------
    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        Author savedAuthor = authorService.save(author);
        return new ResponseEntity<>(savedAuthor, HttpStatus.CREATED); // 201 Created
    }

    // ----------------------------------------------------
    // 2. GET: Tüm Yazarları Listeleme (Read All)
    // URL: GET /api/authors
    // ----------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        List<Author> authors = authorService.findAll();
        return ResponseEntity.ok(authors); // 200 OK
    }

    // ----------------------------------------------------
    // 3. GET: ID'ye Göre Yazar Getirme (Read One)
    // URL: GET /api/authors/{id}
    // ----------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        return authorService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404 Not Found
    }

    // ----------------------------------------------------
    // 4. PUT: Yazar Güncelleme (Update)
    // URL: PUT /api/authors/{id}
    // ----------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author authorDetails) {
        return authorService.findById(id)
                .map(existingAuthor -> {
                    // Verileri güncelle (Entity sınıfınızdaki getter/setter'ların çalıştığından emin olun)
                    existingAuthor.setFirstName(authorDetails.getFirstName());
                    existingAuthor.setLastName(authorDetails.getLastName());
                    existingAuthor.setBiography(authorDetails.getBiography());
                    existingAuthor.setBirthDate(authorDetails.getBirthDate());
                    
                    Author updatedAuthor = authorService.save(existingAuthor);
                    return ResponseEntity.ok(updatedAuthor); // 200 OK
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ----------------------------------------------------
    // 5. DELETE: Yazar Silme (Delete)
    // URL: DELETE /api/authors/{id}
    // ----------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        if (authorService.findById(id).isPresent()) {
            authorService.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build(); // 404 Not Found
    }
}