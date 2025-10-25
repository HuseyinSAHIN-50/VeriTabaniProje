package com.kutuphane.akillikutuphane.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kutuphane.akillikutuphane.entity.Book;
import com.kutuphane.akillikutuphane.entity.Lending;
import com.kutuphane.akillikutuphane.entity.User;
import com.kutuphane.akillikutuphane.repository.LendingRepository;

@Service
public class LendingService {
    
    private final LendingRepository lendingRepository;
    private final BookService bookService;
    private final UserService userService;
    private final FineService fineService; // Ceza servisini kullanıyoruz

    // Constructor Injection: Tüm servisler Spring tarafından sağlanacak
    public LendingService(LendingRepository lendingRepository, BookService bookService, UserService userService, FineService fineService) {
        this.lendingRepository = lendingRepository;
        this.bookService = bookService;
        this.userService = userService;
        this.fineService = fineService;
    }
    
    // ----------------------------------------------------
    // TEMEL CRUD ve OKUMA Metotları
    // ----------------------------------------------------
    public Lending save(Lending lending) { 
        return lendingRepository.save(lending); 
    }
    
    public Optional<Lending> findById(Long id) { 
        return lendingRepository.findById(id); 
    }
    
    public List<Lending> findAll() { 
        return lendingRepository.findAll(); 
    }
    
    public List<Lending> getActiveLoans() { 
        return lendingRepository.findByReturnDateIsNull(); 
    }
    
    // ----------------------------------------------------
    // İŞ AKIŞI: Ödünç Alma (Loan Process)
    // ----------------------------------------------------
    public Lending processLoan(Long userId, Long bookId, LocalDate dueDate) {
        
        Optional<User> userOpt = userService.findById(userId);
        Optional<Book> bookOpt = bookService.findById(bookId);
        
        if (userOpt.isEmpty() || bookOpt.isEmpty()) {
            throw new RuntimeException("Kullanıcı veya kitap bulunamadı.");
        }
        
        Book book = bookOpt.get();
        
        // Stok Kontrolü
        if (book.getStockCount() <= 0) {
            throw new RuntimeException("Stokta kitap kalmadı.");
        }
        
        // Kayıt oluşturma
        Lending newLoan = new Lending();
        newLoan.setUser(userOpt.get());
        newLoan.setBook(book);
        newLoan.setLoanDate(LocalDate.now());
        newLoan.setDueDate(dueDate);
        
        // Stok azaltma
        book.setStockCount(book.getStockCount() - 1);
        bookService.save(book); 
        
        return lendingRepository.save(newLoan);
    }
    
    // ----------------------------------------------------
    // İŞ AKIŞI: İade Etme (Return Process)
    // ----------------------------------------------------
    public Lending processReturn(Long lendingId) {
        Lending loan = lendingRepository.findById(lendingId)
                .orElseThrow(() -> new RuntimeException("Ödünç kaydı bulunamadı."));
        
        if (loan.getReturnDate() != null) {
            throw new RuntimeException("Kitap zaten iade edilmiş.");
        }
        
        // İade Tarihini Belirle
        loan.setReturnDate(LocalDate.now());
        
        // Ceza Kontrolü (İleride buraya SQL Prosedür/Tetikleyici mantığı gelecek)
        if (loan.getReturnDate().isAfter(loan.getDueDate())) {
            // Şimdilik sadece loglayalım. İleride FineService.save() çağrılacak.
            System.out.println("UYARI: Kitap geç iade edildi! Ceza hesaplanmalı.");
        }
        
        // Stok artırma
        Book book = loan.getBook();
        book.setStockCount(book.getStockCount() + 1);
        bookService.save(book);
        
        return lendingRepository.save(loan);
    }
}