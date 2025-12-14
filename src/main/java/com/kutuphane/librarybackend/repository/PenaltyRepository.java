package com.kutuphane.librarybackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kutuphane.librarybackend.entity.Penalty;

public interface PenaltyRepository extends JpaRepository<Penalty, Integer> {
    // Belirli bir kullanıcının cezalarını bul (Loan üzerinden giderek)
    List<Penalty> findByLoan_User_UserId(Integer userId);
}