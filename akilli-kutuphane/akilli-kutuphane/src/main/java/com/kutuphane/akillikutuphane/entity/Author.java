package com.kutuphane.akillikutuphane.entity;

import java.time.LocalDate;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
// import lombok.Data; // Lombok kullanmıyoruz, bu satırı kaldırdık.

@Entity
@Table(name = "AUTHOR")
// @Data // Bu notasyonu siliyoruz/yoruma alıyoruz.
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "biography", columnDefinition = "TEXT")
    private String biography;

    // Yazarın kitapları ile olan One-to-Many ilişkisi (Şimdilik yoruma alınmış hali kalabilir)
    // @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Book> books;
    
    // ----------------------------------------------------
    // MANUEL GETTER ve SETTER METOTLARI (EKSİK KISIM)
    // ----------------------------------------------------

    // ID Metotları
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    // firstName Metotları
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // lastName Metotları
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // birthDate Metotları
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    // biography Metotları
    public String getBiography() {
        return biography;
    }
    public void setBiography(String biography) {
        this.biography = biography;
    }

    // Yorumlanmış OneToMany ilişkisi için metotlar (Kullanılmıyor olabilir, ancak hata vermez)
    /*
    public List<Book> getBooks() {
        return books;
    }
    public void setBooks(List<Book> books) {
        this.books = books;
    }
    */
}