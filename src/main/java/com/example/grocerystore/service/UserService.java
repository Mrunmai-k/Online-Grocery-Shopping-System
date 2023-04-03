package com.example.grocerystore.service;

import com.example.grocerystore.domain.entities.User;
//import com.example.grocerystore.domain.models.service.CategoryServiceModel;
//import com.example.grocerystore.domain.models.service.CategoryServiceModel;
//import com.example.grocerystore.domain.models.service.CategoryServiceModel;
import com.example.grocerystore.domain.models.service.UserServiceModel;

import java.util.List;

public interface UserService {

    UserServiceModel register(UserServiceModel userServiceModel);

    List<UserServiceModel> findAllUsers();

    UserServiceModel findByUsername(String username);

    UserServiceModel findById(String id);

    void updateRole(String id, String role);

    UserServiceModel findUserByUserName(String name);
    
    List<UserServiceModel> findAllByUsers();
    
    List<UserServiceModel> findUser();

	//List<UserServiceModel> findAllByPowerUsers();
	
	public void saveUserWithDefaultRole(User user);
	
	UserServiceModel deleteUser(String id);
	
	List<UserServiceModel> findAllByPowerUsers();
	
	 List<UserServiceModel> findAllFilteredUsers();
	 
	 public void updateResetPasswordToken(String token,String email);
	 
	 public User getByResetPasswordToken(String token);
	 
	 public void updatePassword(User user, String newPassword);
	 
	 public User getByUsername(String username);
}