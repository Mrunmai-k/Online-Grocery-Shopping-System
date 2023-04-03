package com.example.grocerystore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocerystore.domain.entities.User;

public interface PowerUserRepository extends JpaRepository<User,String>
{
	 //Set<Role> findByAuthority(String authority);
	 
	 Optional<User> findByUsername(String username);

	 Optional<User> findByEmail(String email);
}
