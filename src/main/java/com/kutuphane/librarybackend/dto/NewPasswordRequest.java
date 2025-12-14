package com.kutuphane.librarybackend.dto;
import lombok.Data;

@Data
public class NewPasswordRequest {
    private String email;
    private String code;
    private String newPassword;
}