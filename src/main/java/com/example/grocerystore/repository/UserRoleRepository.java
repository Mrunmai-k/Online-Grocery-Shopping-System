package com.example.grocerystore.repository;

import com.example.grocerystore.domain.entities.Role;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<Role, String> {

    Set<Role> findByAuthority(String authority);
    
}