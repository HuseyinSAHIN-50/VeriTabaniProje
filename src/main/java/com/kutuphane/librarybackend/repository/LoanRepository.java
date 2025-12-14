package com.kutuphane.librarybackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kutuphane.librarybackend.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Integer> {
    // Belirli bir kullanıcının ödünç aldığı kitapları listelemek için
    List<Loan> findByUser_UserId(Integer userId);
    
    // Henüz iade edilmemiş kitapları bulmak için (Admin takibi için)
    List<Loan> findByIsReturnedFalse();
    // Prosedürü çağırıyoruz
    @Query(value = "EXEC sp_GetLibraryStats", nativeQuery = true)
    LibraryStats getLibraryStats();
}