package com.kutuphane.akillikutuphane.entity; 

import jakarta.persistence.Column; 
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
// import lombok.Data; // Lombok notasyonu olmadığı için bu satır gereksiz/yorumludur

@Entity 
@Table(name = "CATEGORY") 
// @Data // BU NOTASYONU KALDIRDIK/YORUMA ALDIK
public class Category {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "category_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100) 
    private String name;

    @Column(name = "description")
    private String description;

    // -------------------------------------------------------------------------
    // MANUEL GETTER VE SETTER METOTLARI (Lombok yerine elle eklenmiştir)
    // -------------------------------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}