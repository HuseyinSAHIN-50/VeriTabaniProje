package com.kutuphane.librarybackend.dto;
import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
}