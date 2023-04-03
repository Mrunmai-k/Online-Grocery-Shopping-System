package com.example.grocerystore.service;

//
import com.example.grocerystore.domain.entities.Role;
import com.example.grocerystore.domain.entities.User;

import com.example.grocerystore.domain.models.service.UserServiceModel;
import com.example.grocerystore.repository.ProductRepository;
import com.example.grocerystore.repository.UserRepository;
import com.example.grocerystore.repository.UserRoleRepository;

import com.example.grocerystore.util.error.UsernameAlreadyExistsException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.grocerystore.util.constants.ExceptionMessages.USER_NAME_EXIST_EX_MSG;
import static com.example.grocerystore.util.constants.ValidationErrorMessages.*;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserRoleRepository userRoleRepository,
                           ProductRepository productRepository, ModelMapper modelMapper,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails result = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(DEFAULT_USER_NOT_FOUND_EX_MSG));
        return result;
    }

    @Override
    public UserServiceModel register(UserServiceModel userServiceModel) 
    {
    	if (userRepository.findByUsername(userServiceModel.getUsername())
                .orElse(null) != null) {
            throw new UsernameAlreadyExistsException(USER_NAME_EXIST_EX_MSG);
        }
        User userEntity = this.modelMapper.map(userServiceModel, User.class);

        String encodedPassword = bCryptPasswordEncoder.encode(userServiceModel.getPassword());
        userEntity.setPassword(encodedPassword);
        userEntity.setAuthorities(getRolesForRegistration());

        return modelMapper.map(this.userRepository.saveAndFlush(userEntity), UserServiceModel.class);
    }
    @Override
	public void saveUserWithDefaultRole(User user) 
	{
    	if (userRepository.findByUsername(user.getUsername())
                .orElse(null) != null) {
            throw new UsernameAlreadyExistsException(USER_NAME_EXIST_EX_MSG);
        }
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(); 
		String encodedPwd=encoder.encode(user.getPassword()); 
		user.setPassword(encodedPwd);
		Set<Role> roleUser= userRoleRepository.findByAuthority("ROLE_MODERATOR");
		user.setAuthorities(roleUser); 
		userRepository.save(user);
		
	}
    @Override
	public List<UserServiceModel> findAllByPowerUsers() {
		return this.userRepository.findAll()
                .stream()
                .filter(user -> user.getAuthorities()
                        .stream().anyMatch(roleStream -> roleStream.getAuthority().equals(ROLE_MODERATOR)))
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .collect(Collectors.toList());
	}

    @Override
    public List<UserServiceModel> findAllUsers() {
        return this.userRepository.findAll()
                .stream()
                .map(uEntity -> this.modelMapper.map(uEntity, UserServiceModel.class))
                .collect(Collectors.toList());    			
    }
    @Override
    public List<UserServiceModel> findUser() {
        return this.userRepository.findAll()
                .stream()
                .filter(c->!c.isDeleted())
                .map(c -> this.modelMapper.map(c, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserServiceModel findByUsername(String username) {
        User userEntity = this.userRepository.findByUsername(username).orElse(null);

        return userEntity == null ? null
                : this.modelMapper.map(userEntity, UserServiceModel.class);
    }

    @Override
    public UserServiceModel findById(String id) {
        User userEntity = this.userRepository.findById(id).orElse(null);

        return userEntity == null ? null
                : this.modelMapper.map(userEntity, UserServiceModel.class);
    }
    @Override
    public UserServiceModel deleteUser(String id) {
        User user = this.userRepository.findById(id)
               //.orElseThrow(new UsernameNotFoundException(DEFAULT_USER_NOT_FOUND_EX_MSG));
        		 .orElseThrow(() -> new UsernameNotFoundException(DEFAULT_USER_NOT_FOUND_EX_MSG));
        user.setDeleted(true);
        this.userRepository.save(user);
        this.userRepository.deleteById(id);
        

        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public void updateRole(String id, String role) {
        //here role is role of current loggedin user's role
    	//Id is id of user we want to change the role
    
    	User userEntity = this.userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(DEFAULT_USER_NOT_FOUND_EX_MSG));

        //Below part checks whether we are changing role of root admin if yes then throws Exception
    	
          /*
         boolean isRootAdmin = userEntity.getAuthorities()
                .stream()
                .map(Role::getAuthority)
                .collect(Collectors.toList())
                .contains(ROOT_ADMIN);
        if (isRootAdmin) {
            throw new IllegalArgumentException(DEFAULT_NOT_AUTHORIZE_EX_MSG);
        }*/

        updateUserRole(userEntity, role);
        this.userRepository.saveAndFlush(userEntity);
    }

    @Override
    public UserServiceModel findUserByUserName(String username) {
        return this.userRepository.findByUsername(username)
                .map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .orElseThrow(() -> new UsernameNotFoundException(DEFAULT_USER_NOT_FOUND_EX_MSG));
    }


    private void updateUserRole(User userEntity, String role) {
        Set<Role> newRole = new HashSet<>();
        
        
        switch (role) {
            /*case ROLE_USER:
                newRole.addAll(this.userRoleRepository.findByAuthority(ROLE_USER));
                break;
            case ROLE_MODERATOR:
                newRole.addAll(this.userRoleRepository.findByAuthority(ROLE_MODERATOR));
                newRole.addAll(this.userRoleRepository.findByAuthority(ROLE_USER));
                break;*/
            case ROLE_ADMIN:
            	newRole.addAll(this.userRoleRepository.findByAuthority(ROLE_ADMIN));
                break;
        }
        userEntity.setAuthorities(newRole);
    }

    private Set<Role> getRolesForRegistration() {
        Set<Role> roles = new HashSet<>();

        if(this.userRepository.count() == 0) {
            roles.addAll(this.userRoleRepository.findByAuthority(ROOT_ADMIN));
            roles.addAll(this.userRoleRepository.findByAuthority(ROLE_ADMIN));
            roles.addAll(this.userRoleRepository.findByAuthority(ROLE_ADMIN));
            roles.addAll(this.userRoleRepository.findByAuthority(ROLE_MODERATOR));
            roles.addAll(this.userRoleRepository.findByAuthority(ROLE_USER));
        } else {
            roles.addAll(this.userRoleRepository.findByAuthority(ROLE_USER));
        }

        return roles;
    }

	@Override
	public List<UserServiceModel> findAllByUsers() 
	{
		/*List<String> roles = this.userRoleRepository.findByAuthority(ROLE_USER)
				.stream()
                .map(Role::getAuthority)
                .collect(Collectors.toList());*/
		
		return this.userRepository.findAll()
                .stream()
                .filter(user -> user.getAuthorities()
                        .stream().anyMatch(roleStream -> roleStream.getAuthority().equals(ROLE_USER)))
                .filter(c->!c.isDeleted())
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .collect(Collectors.toList());
		
	}
	@Override
    public List<UserServiceModel> findAllFilteredUsers() {
        return findUser().stream()
                .filter(c->!c.isDeleted())
                .collect(Collectors.toList());
    }
	
	public void updateResetPasswordToken(String token,String email) {
		
		User user= userRepository.findByemail(email);
		if(user !=null) {
		userRepository.save(user);
		user.setResetPasswordToken(token);
		userRepository.save(user);
		}
		else {
			throw new UsernameNotFoundException(DEFAULT_USER_NOT_FOUND_EX_MSG);
			
		}

	}
	
	 public User getByResetPasswordToken(String token) {
	        return userRepository.findByResetPasswordToken(token);
	    }
	 
    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
         
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

	@Override
	public User getByUsername(String username) 
	{
		 User user = this.userRepository.findUsername(username);
		//User user = this.userRepository.findUsername(name); 
		//System.out.println(user);
		return user;
	}

	
	

	
}