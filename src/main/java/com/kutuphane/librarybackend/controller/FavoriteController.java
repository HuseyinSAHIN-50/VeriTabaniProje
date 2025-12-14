package com.kutuphane.librarybackend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kutuphane.librarybackend.entity.Book;
import com.kutuphane.librarybackend.entity.Favorite;
import com.kutuphane.librarybackend.entity.User;
import com.kutuphane.librarybackend.repository.BookRepository;
import com.kutuphane.librarybackend.repository.FavoriteRepository;
import com.kutuphane.librarybackend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteRepository favoriteRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Favorite>> getMyFavorites() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(favoriteRepository.findByUser_UserId(user.getUserId()));
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<String> toggleFavorite(@PathVariable Integer bookId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        
        Optional<Favorite> existing = favoriteRepository.findByUser_UserIdAndBook_BookId(user.getUserId(), bookId);
        
        if (existing.isPresent()) {
            favoriteRepository.delete(existing.get());
            return ResponseEntity.ok("Favorilerden çıkarıldı.");
        } else {
            Book book = bookRepository.findById(bookId).orElseThrow();
            Favorite fav = Favorite.builder().user(user).book(book).build();
            favoriteRepository.save(fav);
            return ResponseEntity.ok("Favorilere eklendi.");
        }
    }
}