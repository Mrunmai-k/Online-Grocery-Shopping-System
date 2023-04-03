/*package com.example.grocerystore.web.controllers;

import org.springframework.stereotype.Controller;
import com.example.grocerystore.service.PowerUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.example.grocerystore.domain.entities.User;
import com.example.grocerystore.domain.models.binding.UserRegisterBindingModel;
import com.example.grocerystore.domain.models.view.UsersViewModel;

import static com.example.grocerystore.util.constants.AppConstants.MODEL;
import static com.example.grocerystore.util.constants.AppConstants.REGISTER;
import static com.example.grocerystore.util.constants.AppConstants.USERS;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import com.example.grocerystore.repository.PowerUserRepository;
import com.example.grocerystore.web.annotations.PageTitle;

@Controller
public class PowerUserController extends BaseController 
{
	@Autowired
	PowerUserService service;
	
	@Autowired
    ModelMapper modelMapper;
	
    @Autowired
    PowerUserRepository repo;
    
  
	
	@GetMapping("/admin/power_users")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle(USERS)
    public ModelAndView renderAllUsersPage() {

        return view("/powerusers-all");
    }
    
    @GetMapping("/api/powerusers")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UsersViewModel> allUsers() {

        return this.service.findAllByPowerUsers()
                .stream()
                .map(serviceModel -> this.modelMapper.map(serviceModel, UsersViewModel.class))
                .collect(Collectors.toList());
    }
	

    @GetMapping("/power_user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle(REGISTER)
	 public ModelAndView renderRegister(@ModelAttribute(name = MODEL) UserRegisterBindingModel model,
	                                       ModelAndView modelAndView)
	 {

	        modelAndView.addObject(MODEL, model);

	        return view("power_user", modelAndView);
	  }
	
	@RequestMapping("/puser")
	//@ResponseBody
	public ModelAndView saveData(User user)
	{
		service.saveUserWithDefaultRole(user);
		
		repo.save(user);
		return redirect("/home");
	
	}

		
 @SuppressWarnings("unused")
	private String htmlEscape(String input){
        input = input.replaceAll("&", "&amp;")
              .replaceAll("<", "&lt;")
              .replaceAll(">", "&gt;")
              .replaceAll("\"", "&quot;");

        return input;
    }
}
*/


