package com.kutuphane.librarybackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kutuphane.librarybackend.entity.Penalty;
import com.kutuphane.librarybackend.entity.User;
import com.kutuphane.librarybackend.repository.PenaltyRepository;
import com.kutuphane.librarybackend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/penalties")
@RequiredArgsConstructor
public class PenaltyController {

    private final PenaltyRepository penaltyRepository;
    private final UserRepository userRepository;

    @GetMapping("/my-penalties")
    public ResponseEntity<List<Penalty>> getMyPenalties() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(penaltyRepository.findByLoan_User_UserId(user.getUserId()));
    }

    // --- YENİ EKLENEN: CEZA ÖDEME METODU ---
    @PostMapping("/pay/{penaltyId}")
    public ResponseEntity<String> payPenalty(@PathVariable Integer penaltyId) {
        Penalty penalty = penaltyRepository.findById(penaltyId)
                .orElseThrow(() -> new RuntimeException("Ceza bulunamadı."));
        
        if (penalty.getPaymentStatus()) {
            return ResponseEntity.badRequest().body("Bu ceza zaten ödenmiş.");
        }

        // Cezayı ödendi olarak işaretle
        penalty.setPaymentStatus(true);
        penaltyRepository.save(penalty);

        return ResponseEntity.ok("Ödeme başarılı! Teşekkürler.");
    }
}