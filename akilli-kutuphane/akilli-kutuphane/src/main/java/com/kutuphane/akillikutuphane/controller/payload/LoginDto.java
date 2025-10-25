package com.kutuphane.akillikutuphane.controller.payload;

// NOT: Lombok olmadığı için Getter/Setter'ları manuel eklemeliyiz.
public class LoginDto {
    private String email;
    private String password;

    // --- Constructor ---
    public LoginDto() {}
    
    // --- Getters and Setters ---
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}