package com.kutuphane.librarybackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kutuphane.librarybackend.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
    // Sadece silinmemiş kitapları getir
    List<Book> findByIsDeletedFalse();
    
    // Arama: Başlık, Yazar veya Kategori içinde ara (Ve silinmemiş olsun)
    List<Book> findByTitleContainingIgnoreCaseAndIsDeletedFalseOrAuthor_AuthorNameContainingIgnoreCaseAndIsDeletedFalseOrCategory_CategoryNameContainingIgnoreCaseAndIsDeletedFalse(String title, String author, String category);
}