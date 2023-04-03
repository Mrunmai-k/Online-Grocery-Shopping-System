package com.example.grocerystore.service;

import static com.example.grocerystore.util.constants.ValidationErrorMessages.DEFAULT_USER_NOT_FOUND_EX_MSG;
import static com.example.grocerystore.util.constants.ValidationErrorMessages.ROLE_MODERATOR;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/*import com.example.grocerystore.domain.entities.User;
import com.example.grocerystore.domain.models.service.UserServiceModel;
import com.example.grocerystore.repository.ProductRepository;
import com.example.grocerystore.repository.PowerUserRepository;
import com.example.grocerystore.repository.UserRoleRepository;
import com.example.grocerystore.domain.entities.Role;

@Service
public class PowerUserServiceImpl implements PowerUserService,UserDetailsService
{

	private final PowerUserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public PowerUserServiceImpl(PowerUserRepository userRepository,
                           UserRoleRepository userRoleRepository,
                           ProductRepository productRepository, ModelMapper modelMapper,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }	
	@Override
	public void saveUserWithDefaultRole(User user) 
	{
		
			BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(); 
			String encodedPwd=encoder.encode(user.getPassword()); 
			user.setPassword(encodedPwd);
			Set<Role> roleUser= userRoleRepository.findByAuthority("ROLE_MODERATOR");
			user.setAuthorities(roleUser); 
			userRepository.save(user);
		
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails result = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(DEFAULT_USER_NOT_FOUND_EX_MSG));
        return result;
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
}
*/