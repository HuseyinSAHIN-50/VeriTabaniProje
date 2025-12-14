package com.kutuphane.librarybackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kutuphane.librarybackend.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    // Rol ismine göre arama yapmak için (Örn: ROLE_USER var mı?)
    Optional<Role> findByRoleName(String roleName);
}