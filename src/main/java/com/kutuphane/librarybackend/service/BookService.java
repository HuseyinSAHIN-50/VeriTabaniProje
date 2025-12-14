package com.kutuphane.librarybackend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kutuphane.librarybackend.dto.BookRequest;
import com.kutuphane.librarybackend.entity.Author;
import com.kutuphane.librarybackend.entity.Book;
import com.kutuphane.librarybackend.entity.Category;
import com.kutuphane.librarybackend.repository.AuthorRepository;
import com.kutuphane.librarybackend.repository.BookRepository;
import com.kutuphane.librarybackend.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    // --- KİTAP EKLEME ---
    public String addBook(BookRequest request) {
        // 1. Yazar kontrolü (Yoksa oluştur)
        // DÜZELTME: new Author(...) yerine setter kullanıldı
        Author author = authorRepository.findByAuthorName(request.getAuthorName())
                .orElseGet(() -> {
                    Author newAuthor = new Author();
                    newAuthor.setAuthorName(request.getAuthorName());
                    return authorRepository.save(newAuthor);
                });

        // 2. Kategori kontrolü (Yoksa oluştur)
        // DÜZELTME: new Category(...) yerine setter kullanıldı
        Category category = categoryRepository.findByCategoryName(request.getCategoryName())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setCategoryName(request.getCategoryName());
                    return categoryRepository.save(newCategory);
                });

        // 3. Kitabı Oluştur
        Book book = Book.builder()
                .title(request.getTitle())
                .isbn(request.getIsbn())
                .publicationYear(request.getPublicationYear())
                .stockQuantity(request.getStockQuantity())
                .category(category)
                .author(author)
                .isDeleted(false) // Varsayılan aktif
                .build();

        bookRepository.save(book);
        return "Kitap başarıyla eklendi.";
    }

    // --- ARAMA METODU ---
    public List<Book> searchBooks(String query) {
        return bookRepository.findByTitleContainingIgnoreCaseAndIsDeletedFalseOrAuthor_AuthorNameContainingIgnoreCaseAndIsDeletedFalseOrCategory_CategoryNameContainingIgnoreCaseAndIsDeletedFalse(query, query, query);
    }

    // --- SOFT DELETE (GÜVENLİ SİLME) ---
    public void deleteBook(Integer id) { 
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kitap bulunamadı"));
        
        book.setIsDeleted(true); // Silme, sadece işaretle
        bookRepository.save(book);
    }

    // --- LİSTELEME (Sadece silinmemişleri getir) ---
    public List<Book> getAllBooks() { 
        return bookRepository.findByIsDeletedFalse(); 
    }
    
    // --- KİTAP GÜNCELLEME ---
    public String updateBook(Integer id, BookRequest request) {
         Book book = bookRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("Kitap bulunamadı"));
         
         // Yazar değiştiyse güncelle
         if (request.getAuthorName() != null && !request.getAuthorName().isEmpty()) {
             Author author = authorRepository.findByAuthorName(request.getAuthorName())
                .orElseGet(() -> {
                    Author newAuthor = new Author();
                    newAuthor.setAuthorName(request.getAuthorName());
                    return authorRepository.save(newAuthor);
                });
             book.setAuthor(author);
         }
         
         // Kategori değiştiyse güncelle
         if (request.getCategoryName() != null && !request.getCategoryName().isEmpty()) {
            Category category = categoryRepository.findByCategoryName(request.getCategoryName())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setCategoryName(request.getCategoryName());
                    return categoryRepository.save(newCategory);
                });
            book.setCategory(category);
         }

         book.setTitle(request.getTitle());
         book.setIsbn(request.getIsbn());
         book.setStockQuantity(request.getStockQuantity());
         book.setPublicationYear(request.getPublicationYear());
         
         bookRepository.save(book);
         return "Kitap başarıyla güncellendi.";
    }
}