package com.kutuphane.librarybackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer bookId;

    @Column(nullable = false)
    private String title;

    @Column(unique = true)
    private String isbn;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    // Soft Delete Alanı
    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    // İlişki: Bir kitabın bir yazarı olur
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    // İlişki: Bir kitabın bir kategorisi olur
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}