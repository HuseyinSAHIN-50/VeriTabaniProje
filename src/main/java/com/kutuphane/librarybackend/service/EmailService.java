package com.kutuphane.librarybackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // @Async: Mail gönderme işlemi uzun sürebilir (2-3 sn), 
    // kullanıcının işlemini bekletmemek için bunu arka planda yaparız.
    @Async 
    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("seninmailin@gmail.com"); // Buraya kendi mailini yaz
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
        System.out.println("Mail gönderildi: " + toEmail);
    }
}