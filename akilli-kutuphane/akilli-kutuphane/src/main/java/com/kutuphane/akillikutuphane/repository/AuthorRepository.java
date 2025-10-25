package com.kutuphane.akillikutuphane.repository;

import com.kutuphane.akillikutuphane.entity.Author; // Author Entity'sini import edin
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    
    // Özel bir metot ekleyebiliriz (Örn: Yazarın adına ve soyadına göre bulma)
    Author findByFirstNameAndLastName(String firstName, String lastName);
}