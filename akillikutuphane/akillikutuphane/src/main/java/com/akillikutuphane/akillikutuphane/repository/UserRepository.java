package com.akillikutuphane.akillikutuphane.repository;

import com.akillikutuphane.akillikutuphane.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
