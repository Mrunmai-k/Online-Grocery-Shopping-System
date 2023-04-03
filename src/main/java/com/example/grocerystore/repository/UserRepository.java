package com.example.grocerystore.repository;

import com.example.grocerystore.domain.entities.User;
//import com.example.grocerystore.domain.models.service.UserServiceModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
//import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
    
    @Query("SELECT c FROM User c WHERE c.email = ?1")
    public User findByemail(String email);  
    
    
    @Query("UPDATE User u SET u.enable=true WHERE u.id= ?1")
    @Modifying
    public void enable(Integer id);
    
    public User findByResetPasswordToken(String token);
   
    @Query("SELECT u FROM User u WHERE u.username = ?1")
    public User findUsername(String username);	
    
    	
   }
    
    