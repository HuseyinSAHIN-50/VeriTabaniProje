package com.kutuphane.librarybackend.repository;

public interface LibraryStats {
    Integer getTotalBooks();
    Integer getTotalUsers();
    Integer getActiveLoans();
}