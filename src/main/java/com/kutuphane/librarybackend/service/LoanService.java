package com.kutuphane.librarybackend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
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

    // --- KİTAP ÖDÜNÇ ALMA ---
    @Transactional
    public String borrowBook(LoanRequest request, String userEmail) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Kitap bulunamadı."));

        if (book.getStockQuantity() <= 0) {
            throw new RuntimeException("Üzgünüz, bu kitap stokta kalmadı.");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setBorrowDate(LocalDateTime.now());
        
        // TEST İÇİN: 1 Dakika süre veriyoruz
        loan.setDueDate(LocalDateTime.now().plusMinutes(1)); 
        
        loan.setIsReturned(false);

        loanRepository.save(loan);

        book.setStockQuantity(book.getStockQuantity() - 1);
        bookRepository.save(book);

        // Bilgilendirme Maili
        try {
            String emailSubject = "E-Kütüphane: Kitap Ödünç Alındı";
            String emailBody = "Sayın " + user.getFirstName() + ",\n\n" +
                    "'" + book.getTitle() + "' kitabını başarıyla ödünç aldınız.\n" +
                    "Son Teslim Tarihi: " + loan.getDueDate().toLocalDate() + "\n\n" +
                    "Keyifli okumalar dileriz.";
            emailService.sendSimpleEmail(user.getEmail(), emailSubject, emailBody);
        } catch (Exception e) {
            System.err.println("Mail gönderilemedi: " + e.getMessage());
        }
        
        return "Kitap başarıyla ödünç alındı.";
    }

    // --- KİTAP İADE ETME ---
    @Transactional
    public String returnBook(Integer loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Kayıt bulunamadı."));

        if (loan.getIsReturned()) {
            throw new RuntimeException("Bu kitap zaten iade edilmiş.");
        }

        loan.setIsReturned(true);
        loan.setReturnDate(LocalDateTime.now());
        loanRepository.save(loan); // SQL Trigger burada devreye girip asıl cezayı yazar

        Book book = loan.getBook();
        book.setStockQuantity(book.getStockQuantity() + 1);
        bookRepository.save(book);

        return "Kitap başarıyla iade edildi.";
    }

    public List<Loan> getUserLoans(Integer userId) {
        return loanRepository.findByUser_UserId(userId);
    }

    // --- OTOMATİK GECİKME UYARISI ---
    // Her dakikanın başında çalışır
    @Scheduled(cron = "0 * * * * *") 
    public void sendOverdueNotifications() {
        System.out.println("Zamanlayıcı çalıştı: Gecikmiş kitaplar kontrol ediliyor...");
        
        List<Loan> overdueLoans = loanRepository.findOverdueLoans(LocalDateTime.now());

        for (Loan loan : overdueLoans) {
            try {
                String email = loan.getUser().getEmail();
                String subject = "GECİKME UYARISI: Kitap İade Süresi Doldu";
                String body = "Sayın " + loan.getUser().getFirstName() + ",\n\n" +
                              "'" + loan.getBook().getTitle() + "' kitabınızın süresi dolmuştur.\n" +
                              "Lütfen en kısa sürede iade ediniz. Şu anki gecikme cezanız sistemde işlemektedir.\n\n" +
                              "E-Kütüphane Sistemi";
                
                emailService.sendSimpleEmail(email, subject, body);
                System.out.println("Uyarı maili gönderildi: " + email);
            } catch (Exception e) {
                System.err.println("Mail hatası (" + loan.getLoanId() + "): " + e.getMessage());
            }
        }
    }
}