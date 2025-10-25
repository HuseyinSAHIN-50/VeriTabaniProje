package com.kutuphane.akillikutuphane.entity;

import java.math.BigDecimal;
import java.time.LocalDate; // Para birimi için BigDecimal kullanıyoruz

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "FINE")
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ceza, belirli bir ödünç verme işlemiyle ilişkilidir (One-to-One veya Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lending_id", nullable = false)
    private Lending lending;

    @Column(name = "fine_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal fineAmount; // Ceza Miktarı

    @Column(name = "fine_date", nullable = false)
    private LocalDate fineDate;

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid = false; // Ödendi mi?

    // ----------------------------------------------------
    // MANUEL GETTER ve SETTER METOTLARI
    // ----------------------------------------------------

    // Not: Tüm metotları eklemelisiniz. Kısaltılmış örnek:
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Lending getLending() { return lending; }
    public void setLending(Lending lending) { this.lending = lending; }
    public BigDecimal getFineAmount() { return fineAmount; }
    public void setFineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; }
    public LocalDate getFineDate() { return fineDate; }
    public void setFineDate(LocalDate fineDate) { this.fineDate = fineDate; }
    public Boolean getIsPaid() { return isPaid; }
    public void setIsPaid(Boolean isPaid) { this.isPaid = isPaid; }
}