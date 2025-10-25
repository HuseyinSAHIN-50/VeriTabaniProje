package com.kutuphane.akillikutuphane.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kutuphane.akillikutuphane.entity.Lending;
import com.kutuphane.akillikutuphane.service.LendingService;

@RestController
@RequestMapping("/api/loans") // Ödünç verme işlemleri için temel URL
public class LendingController {

    private final LendingService lendingService;

    public LendingController(LendingService lendingService) {
        this.lendingService = lendingService;
    }
    
    // ----------------------------------------------------
    // 1. POST: Ödünç Alma API'si (İŞLEM BAŞLATMA)
    // URL: POST /api/loans/loan
    // ----------------------------------------------------
    @PostMapping("/loan")
    public ResponseEntity<?> loanBook(@RequestBody Map<String, Object> request) {
        try {
            Long userId = ((Number) request.get("userId")).longValue();
            Long bookId = ((Number) request.get("bookId")).longValue();
            
            // DueDate'i (İade Tarihi) 14 gün sonrası olarak belirleyelim
            LocalDate dueDate = LocalDate.now().plusDays(14); 

            Lending newLoan = lendingService.processLoan(userId, bookId, dueDate);
            return new ResponseEntity<>(newLoan, HttpStatus.CREATED); // 201 Created
            
        } catch (RuntimeException e) {
            // Service katmanından gelen stok hatası, kullanıcı/kitap bulunamadı hatası vb.
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    // ----------------------------------------------------
    // 2. PUT: İade Etme API'si (İŞLEM SONLANDIRMA)
    // URL: PUT /api/loans/return/{lendingId}
    // ----------------------------------------------------
    @PutMapping("/return/{lendingId}")
    public ResponseEntity<?> returnBook(@PathVariable Long lendingId) {
        try {
            Lending returnedLoan = lendingService.processReturn(lendingId);
            return ResponseEntity.ok(returnedLoan); // 200 OK
        } catch (RuntimeException e) {
            // Ödünç kaydı bulunamadı, zaten iade edilmiş vb. hatalar
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }
    
    // ----------------------------------------------------
    // 3. GET: Aktif Ödünçleri Listeleme (READ)
    // URL: GET /api/loans/active
    // ----------------------------------------------------
    @GetMapping("/active")
    public ResponseEntity<List<Lending>> getActiveLoans() {
        List<Lending> activeLoans = lendingService.getActiveLoans();
        return ResponseEntity.ok(activeLoans); // 200 OK
    }
}