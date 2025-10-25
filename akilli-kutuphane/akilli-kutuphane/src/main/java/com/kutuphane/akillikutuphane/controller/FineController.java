package com.kutuphane.akillikutuphane.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kutuphane.akillikutuphane.entity.Fine;
import com.kutuphane.akillikutuphane.service.FineService;

@RestController
@RequestMapping("/api/fines") // Ceza işlemleri için temel URL
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    // ----------------------------------------------------
    // 1. GET: Tüm Cezaları Listeleme (Admin İşlemi)
    // URL: GET /api/fines
    // ----------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Fine>> getAllFines() {
        List<Fine> fines = fineService.findAll();
        return ResponseEntity.ok(fines); 
    }
    
    // ----------------------------------------------------
    // 2. GET: Ödenmemiş Cezaları Listeleme (Admin İşlemi)
    // URL: GET /api/fines/unpaid
    // ----------------------------------------------------
    @GetMapping("/unpaid")
    public ResponseEntity<List<Fine>> getUnpaidFines() {
        List<Fine> unpaidFines = fineService.getUnpaidFines();
        return ResponseEntity.ok(unpaidFines); 
    }
    
    // ----------------------------------------------------
    // 3. PUT: Ceza Ödeme İşlemi (İŞ AKIŞI)
    // Ceza kaydını "isPaid = true" olarak günceller
    // URL: PUT /api/fines/pay/{id}
    // ----------------------------------------------------
    @PutMapping("/pay/{id}")
    public ResponseEntity<Fine> markAsPaid(@PathVariable Long id) {
        Optional<Fine> fineOpt = fineService.findById(id);

        if (fineOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Fine fine = fineOpt.get();
        if (fine.getIsPaid()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409 Conflict: Zaten ödenmiş
        }

        fine.setIsPaid(true); // Ödendi olarak işaretle
        Fine updatedFine = fineService.save(fine);
        
        return ResponseEntity.ok(updatedFine); // 200 OK
    }
}