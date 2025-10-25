package com.kutuphane.akillikutuphane.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kutuphane.akillikutuphane.entity.Fine;
import com.kutuphane.akillikutuphane.repository.FineRepository;

@Service
public class FineService {
    
    private final FineRepository fineRepository;
    
    // Constructor Injection
    public FineService(FineRepository fineRepository) {
        this.fineRepository = fineRepository;
    }
    
    // CRUD Metotları
    public Fine save(Fine fine) { 
        return fineRepository.save(fine); 
    }
    
    public Optional<Fine> findById(Long id) { 
        return fineRepository.findById(id); 
    }
    
    public List<Fine> findAll() {
        return fineRepository.findAll();
    }
    
    // İş Mantığı: Ödenmemiş cezaları getirme
    public List<Fine> getUnpaidFines() { 
        return fineRepository.findByIsPaidFalse(); 
    }
    
    // ... Diğer metotlar (Örn: Ceza ödeme işlemi) ...
}