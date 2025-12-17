package com.kutuphane.librarybackend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kutuphane.librarybackend.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Integer> {
    
    // Kullanıcının tüm geçmişi
    List<Loan> findByUser_UserId(Integer userId);

    // --- YENİ: Kullanıcının sadece İADE ETMEDİĞİ (Elindeki) kitapları getir ---
    List<Loan> findByUser_UserIdAndIsReturnedFalse(Integer userId);
    
    // Admin için iade edilmemişler
    List<Loan> findByIsReturnedFalse();

    // Gecikenleri bul (Otomatik Mail İçin)
    @Query("SELECT l FROM Loan l WHERE l.isReturned = false AND l.dueDate < :now")
    List<Loan> findOverdueLoans(LocalDateTime now);

    // İstatistik Prosedürü
    @Query(value = "EXEC sp_GetLibraryStats", nativeQuery = true)
    LibraryStats getLibraryStats();
}