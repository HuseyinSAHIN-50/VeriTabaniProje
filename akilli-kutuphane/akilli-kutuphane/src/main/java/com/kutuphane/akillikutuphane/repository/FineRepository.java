package com.kutuphane.akillikutuphane.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kutuphane.akillikutuphane.entity.Fine;

public interface FineRepository extends JpaRepository<Fine, Long> {
    
    // İş Mantığı İçin Gerekli Metot: Ödenmemiş cezaları listeleme
    // isPaid alanı false olan kayıtları getirir.
    List<Fine> findByIsPaidFalse();

    // Belirli bir ödünç işlemine ait cezaları bulma
    List<Fine> findByLendingId(Long lendingId);
}