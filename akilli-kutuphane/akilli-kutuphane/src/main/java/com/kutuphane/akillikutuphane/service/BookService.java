package com.kutuphane.akillikutuphane.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kutuphane.akillikutuphane.entity.Book;
import com.kutuphane.akillikutuphane.repository.BookRepository;

@Service
public class BookService {

    private final BookRepository bookRepository;
    
    // Not: Author ve Category servislerini de enjekte ederek iş mantığını burada yürütebiliriz.
    // private final AuthorService authorService;
    // private final CategoryService categoryService;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // CRUD Metotları
    public Book save(Book book) {
        // İŞ KURALI NOTU: Kaydetmeden önce Author ve Category'nin var olup olmadığını kontrol edebiliriz.
        return bookRepository.save(book);
    }

    public Optional<Book> findById(Long id) {
        // Kitap detaylarını getirirken Author ve Category bilgileri de otomatik olarak JOIN ile gelecektir (LAZY/EAGER ayarına bağlı).
        return bookRepository.findById(id);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }
    
    public List<Book> searchByTitle(String title) {
        // Kitap arama (Proje gereksinimi)
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}