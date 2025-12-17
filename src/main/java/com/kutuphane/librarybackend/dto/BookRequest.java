package com.kutuphane.librarybackend.dto;

import lombok.Data;

@Data
public class BookRequest {
    private String title;
    private String isbn;
    private Integer publicationYear;
    private Integer stockQuantity;
    private String authorName;
    private String categoryName;
}