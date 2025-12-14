package com.kutuphane.librarybackend.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kutuphane.librarybackend.dto.LoanRequest;
import com.kutuphane.librarybackend.entity.Book;
import com.kutuphane.librarybackend.entity.Loan;
import com.kutuphane.librarybackend.entity.User;
import com.kutuphane.librarybackend.repository.BookRepository;
import com.kutuphane.librarybackend.repository.LoanRepository;
import com.kutuphane.librarybackend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final EmailService emailService;
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional // İşlem sırasında hata olursa veritabanını eski haline getirir (Rollback)
    public String borrowBook(LoanRequest request, String userEmail) {
        // 1. Kitabı Bul
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Kitap bulunamadı."));

        // 2. Stok Kontrolü
        if (book.getStockQuantity() <= 0) {
            throw new RuntimeException("Üzgünüz, bu kitap stokta kalmadı.");
        }

        // 3. Kullanıcıyı Bul (Token'dan gelen email ile)
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));

        // 4. Ödünç Kaydı Oluştur
        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setBorrowDate(LocalDateTime.now());
        loan.setDueDate(LocalDateTime.now().plusDays(15)); // 15 gün süre
        loan.setIsReturned(false);

        loanRepository.save(loan);

        // 5. Stoktan Düş
        book.setStockQuantity(book.getStockQuantity() - 1);
        bookRepository.save(book);

        String emailSubject = "Kütüphane: Ödünç Alma İşlemi Başarılı";
String emailBody = "Sayın " + user.getFirstName() + ",\n\n" +
        "'" + book.getTitle() + "' adlı kitabı başarıyla ödünç aldınız.\n" +
        "Son Teslim Tarihi: " + loan.getDueDate().toLocalDate() + "\n\n" +
        "Lütfen kitabı zamanında iade ediniz.\n\n" +
        "İyi okumalar!";

// Arka planda mail at (Hata olsa bile akışı bozmaz)
emailService.sendSimpleEmail(user.getEmail(), emailSubject, emailBody);
        return "Kitap başarıyla ödünç alındı. Son teslim tarihi: " + loan.getDueDate().toLocalDate();
    }
        @Transactional
    public String returnBook(Integer loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Kayıt bulunamadı."));

        if (loan.getIsReturned()) {
            throw new RuntimeException("Bu kitap zaten iade edilmiş.");
        }

        // İade bilgilerini güncelle
        loan.setIsReturned(true);
        loan.setReturnDate(LocalDateTime.now());
        loanRepository.save(loan); // BURADA TRIGGER DEVREYE GİRECEK!

        // Stok miktarını geri artır (Kitap kütüphaneye döndü)
        Book book = loan.getBook();
        book.setStockQuantity(book.getStockQuantity() + 1);
        bookRepository.save(book);

        return "Kitap başarıyla iade edildi.";
    }

    // 2. KULLANICININ ÖDÜNÇ ALDIKLARINI LİSTELEME
    public java.util.List<Loan> getUserLoans(Integer userId) {
        return loanRepository.findByUser_UserId(userId);
    }
}