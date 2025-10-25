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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kutuphane.akillikutuphane.entity.Author;
import com.kutuphane.akillikutuphane.entity.Book;
import com.kutuphane.akillikutuphane.entity.Category;
import com.kutuphane.akillikutuphane.service.AuthorService;
import com.kutuphane.akillikutuphane.service.BookService;
import com.kutuphane.akillikutuphane.service.CategoryService;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    public BookController(BookService bookService, AuthorService authorService, CategoryService categoryService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    // ----------------------------------------------------
    // 1. POST: Yeni Kitap Oluşturma (Create)
    // URL: POST /api/books
    // ----------------------------------------------------
    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        // Kitap kaydetmeden önce, yazar ve kategorinin gerçekten var olduğunu doğrulayın
        Author author = authorService.findById(book.getAuthor().getId())
                .orElse(null);
        Category category = categoryService.findById(book.getCategory().getId())
                .orElse(null);

        if (author == null || category == null) {
            return new ResponseEntity<>("Yazar veya Kategori bulunamadı.", HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
        
        // İlişki nesnelerini ayarlayın
        book.setAuthor(author);
        book.setCategory(category);

        Book savedBook = bookService.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED); // 201 Created
    }

    // ----------------------------------------------------
    // 2. GET: Tüm Kitapları Listeleme veya Başlığa Göre Arama (Read/Search)
    // URL: GET /api/books?title=roman
    // ----------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(@RequestParam(required = false) String title) {
        List<Book> books;
        if (title != null && !title.isEmpty()) {
            books = bookService.searchByTitle(title);
        } else {
            books = bookService.findAll();
        }
        return ResponseEntity.ok(books); // 200 OK
    }
    
    // ----------------------------------------------------
    // 3. GET: ID'ye Göre Kitap Getirme (Read One)
    // URL: GET /api/books/{id}
    // ----------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404 Not Found
    }

    // ----------------------------------------------------
    // 4. PUT: Kitap Güncelleme (Update)
    // URL: PUT /api/books/{id}
    // ----------------------------------------------------
    @PutMapping("/{id}")
public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
    return bookService.findById(id) // Önce ID ile kitabı bul
            .map(existingBook -> {
                
                // İlişki ID'lerini doğrulama
                Author author = authorService.findById(bookDetails.getAuthor().getId()).orElse(null);
                Category category = categoryService.findById(bookDetails.getCategory().getId()).orElse(null);

                if (author == null || category == null) {
                    // Geçersiz ilişki ID'leri için 400 Bad Request döner
                    // Hata tipini Book yerine Void yaparak dönüş uyumsuzluğunu giderdik
                    return new ResponseEntity<Book>(HttpStatus.BAD_REQUEST); 
                }
                
                // Verileri güncelle
                existingBook.setTitle(bookDetails.getTitle());
                existingBook.setIsbn(bookDetails.getIsbn());
                existingBook.setPublicationYear(bookDetails.getPublicationYear());
                existingBook.setStockCount(bookDetails.getStockCount());
                
                // İlişkileri güncelle
                existingBook.setAuthor(author);
                existingBook.setCategory(category);

                Book updatedBook = bookService.save(existingBook);
                return ResponseEntity.ok(updatedBook); // 200 OK
            })
            // Eğer kitap bulunamazsa 404 döner. (Ln 109'daki hatayı çözmek için tip belirtilir)
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); 
}

    // ----------------------------------------------------
    // 5. DELETE: Kitap Silme (Delete) <-- EKLENEN METOT
    // URL: DELETE /api/books/{id}
    // ----------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (bookService.findById(id).isPresent()) {
            bookService.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build(); // 404 Not Found
    }
}