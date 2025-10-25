package com.kutuphane.akillikutuphane.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kutuphane.akillikutuphane.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    // Proje gereksinimine uygun özel sorgu metotları (JOIN'leri temsil eder)
    
    // Başlığa göre kitap arama
    List<Book> findByTitleContainingIgnoreCase(String title);

    // ISBN'e göre kitap bulma
    Book findByIsbn(String isbn);
    
    // Yazar ID'sine göre kitapları listeleme
    List<Book> findByAuthorId(Long authorId);
}