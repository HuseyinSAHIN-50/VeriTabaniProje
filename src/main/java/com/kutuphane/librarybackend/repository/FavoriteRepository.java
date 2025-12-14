package com.kutuphane.librarybackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kutuphane.librarybackend.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    List<Favorite> findByUser_UserId(Integer userId);
    Optional<Favorite> findByUser_UserIdAndBook_BookId(Integer userId, Integer bookId);
}