package com.kutuphane.librarybackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // EKLENDİ
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kutuphane.librarybackend.dto.LoanRequest;
import com.kutuphane.librarybackend.entity.Loan;
import com.kutuphane.librarybackend.entity.User;
import com.kutuphane.librarybackend.repository.LibraryStats;
import com.kutuphane.librarybackend.repository.UserRepository;
import com.kutuphane.librarybackend.service.LoanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
    private final com.kutuphane.librarybackend.repository.LoanRepository loanRepository;
    private final LoanService loanService;
    private final UserRepository userRepository; // EKLENDİ

    @PostMapping("/borrow")
    public ResponseEntity<String> borrowBook(@RequestBody LoanRequest request) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(loanService.borrowBook(request, userEmail));
    }

    //İade Etme
    @PostMapping("/return/{loanId}")
    public ResponseEntity<String> returnBook(@PathVariable Integer loanId) {
        return ResponseEntity.ok(loanService.returnBook(loanId));
    }

    //Ödünçlerimi Gör
    @GetMapping("/my-loans")
    public ResponseEntity<List<Loan>> getMyLoans() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(loanService.getUserLoans(user.getUserId()));
    }
    @GetMapping("/stats")
    public ResponseEntity<LibraryStats> getStats() {
        return ResponseEntity.ok(loanRepository.getLibraryStats());
    }
}