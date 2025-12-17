package com.kutuphane.librarybackend.entity;

import java.math.BigDecimal; // ÖNEMLİ: Bu import olmalı
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Penalties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Penalty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "penalty_id")
    private Integer penaltyId;

    @OneToOne
    @JoinColumn(name = "loan_id", unique = true, nullable = false)
    private Loan loan;

    // --- DÜZELTME BURADA ---
    // Eğer burada "Double" yazıyorsa hatayı o veriyor.
    // Burası "BigDecimal" olmalı.
    @Column(name = "penalty_amount", nullable = false)
    private BigDecimal penaltyAmount; 
    // -----------------------

    @Column(name = "payment_status")
    private Boolean paymentStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}