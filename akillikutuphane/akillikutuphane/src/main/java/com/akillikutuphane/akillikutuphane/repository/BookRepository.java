package com.akillikutuphane.akillikutuphane.repository;

import com.akillikutuphane.akillikutuphane.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
