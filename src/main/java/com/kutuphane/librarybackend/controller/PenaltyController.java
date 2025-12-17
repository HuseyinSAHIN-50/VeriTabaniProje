package com.kutuphane.librarybackend.controller;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kutuphane.librarybackend.entity.Loan;
import com.kutuphane.librarybackend.entity.Penalty;
import com.kutuphane.librarybackend.entity.User;
import com.kutuphane.librarybackend.repository.LoanRepository; // Eklendi
import com.kutuphane.librarybackend.repository.PenaltyRepository;
import com.kutuphane.librarybackend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/penalties")
@RequiredArgsConstructor
public class PenaltyController {

    private final PenaltyRepository penaltyRepository;
    private final UserRepository userRepository;
    private final LoanRepository loanRepository; // Eklendi

    @GetMapping("/my-penalties")
    public ResponseEntity<List<Penalty>> getMyPenalties() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        
        // 1. Veritabanındaki (Kesinleşmiş) cezaları getir
        // Listeyi değiştirilebilir (mutable) yapmak için ArrayList içine alıyoruz
        List<Penalty> allPenalties = new ArrayList<>(penaltyRepository.findByLoan_User_UserId(user.getUserId()));

        // 2. Şu an elinde olan ve GECİKMİŞ kitapları bul
        List<Loan> activeLoans = loanRepository.findByUser_UserIdAndIsReturnedFalse(user.getUserId());

        for (Loan loan : activeLoans) {
            // Eğer teslim tarihi geçmişse
            if (loan.getDueDate().isBefore(LocalDateTime.now())) {
                
                // Geciken süreyi hesapla (Dakika bazlı test yaptığımız için dakika alıyoruz)
                long minutesLate = Duration.between(loan.getDueDate(), LocalDateTime.now()).toMinutes();
                
                if (minutesLate > 0) {
                    // Ceza Miktarı: Dakikası 10 TL (SQL Trigger ile aynı mantıkta)
                    BigDecimal currentAmount = BigDecimal.valueOf(minutesLate * 10);

                    // Listede göstermek için GEÇİCİ bir ceza nesnesi oluşturuyoruz
                    // Bu nesne veritabanına kaydedilmez, sadece ekranda görünür.
                    Penalty tempPenalty = Penalty.builder()
                            .penaltyId(null) // ID yok, çünkü henüz DB'de değil
                            .loan(loan)
                            .penaltyAmount(currentAmount)
                            .paymentStatus(false)
                            .createdAt(LocalDateTime.now())
                            .build();
                    
                    allPenalties.add(tempPenalty);
                }
            }
        }

        return ResponseEntity.ok(allPenalties);
    }

    @PostMapping("/pay/{penaltyId}")
    public ResponseEntity<String> payPenalty(@PathVariable Integer penaltyId) {
        if(penaltyId == null) {
            return ResponseEntity.badRequest().body("Bu ceza henüz kesinleşmemiştir. Lütfen önce kitabı iade ediniz.");
        }

        Penalty penalty = penaltyRepository.findById(penaltyId)
                .orElseThrow(() -> new RuntimeException("Ceza bulunamadı."));
        
        if (penalty.getPaymentStatus()) {
            return ResponseEntity.badRequest().body("Bu ceza zaten ödenmiş.");
        }

        penalty.setPaymentStatus(true);
        penaltyRepository.save(penalty);

        return ResponseEntity.ok("Ödeme başarılı! Teşekkürler.");
    }
}