package com.kutuphane.akillikutuphane.service;

import com.kutuphane.akillikutuphane.entity.Author;
import com.kutuphane.akillikutuphane.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // ----------------------------------------------------
    // CRUD İşlemleri için Temel Metotlar
    // ----------------------------------------------------

    public Author save(Author author) {
        return authorRepository.save(author);
    }

    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }

    public Author findByFirstNameAndLastName(String firstName, String lastName) {
        return authorRepository.findByFirstNameAndLastName(firstName, lastName);
    }
}