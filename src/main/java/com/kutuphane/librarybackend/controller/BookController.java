package com.kutuphane.librarybackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kutuphane.librarybackend.dto.BookRequest;
import com.kutuphane.librarybackend.entity.Book;
import com.kutuphane.librarybackend.service.BookService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // Tüm Kitapları Listeleme
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }
    // SİLME ENDPOINT'İ
@PreAuthorize("hasRole('ADMIN')")
@DeleteMapping("/{id}")
public ResponseEntity<String> deleteBook(@PathVariable Integer id) {
    bookService.deleteBook(id);
    return ResponseEntity.ok("Kitap silindi.");
}

// --- KİTAP EKLEME (URL YÖNTEMİ) ---
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<String> addBook(@RequestBody BookRequest request) {
        String result = bookService.addBook(request);
        return ResponseEntity.ok(result);
    }

    // --- KİTAP GÜNCELLEME (URL YÖNTEMİ) ---
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Integer id, @RequestBody BookRequest request) {
        String result = bookService.updateBook(id, request);
        return ResponseEntity.ok(result);
    }

    // YENİ: Arama Endpoint'i
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String query) {
        return ResponseEntity.ok(bookService.searchBooks(query));
    }
}