package com.kutuphane.akillikutuphane.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kutuphane.akillikutuphane.entity.Lending;

public interface LendingRepository extends JpaRepository<Lending, Long> {
    
    // İş Mantığı İçin Gerekli Metot: Aktif ödünçleri listeleme (Henüz iade edilmemiş olanlar)
    // returnDate alanı null olan kayıtları getirir.
    List<Lending> findByReturnDateIsNull();

    // Bir kullanıcının aktif ödünçlerini bulma
    List<Lending> findByUserIdAndReturnDateIsNull(Long userId);
    
    // Bir kitabın aktif ödünçlerini bulma
    List<Lending> findByBookIdAndReturnDateIsNull(Long bookId);
}