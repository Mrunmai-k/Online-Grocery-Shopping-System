package com.example.grocerystore.web.controllers;

import com.example.grocerystore.domain.entities.Feedback;
import com.example.grocerystore.domain.entities.User;
import com.example.grocerystore.domain.models.binding.UserRegisterBindingModel;

import com.example.grocerystore.domain.models.service.UserServiceModel;

import com.example.grocerystore.domain.models.view.UsersViewModel;
import com.example.grocerystore.repository.FeedbackRepository;
import com.example.grocerystore.repository.UserRepository;

import com.example.grocerystore.service.UserService;
import org.modelmapper.ModelMapper;
import com.example.grocerystore.web.annotations.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.grocerystore.util.constants.AppConstants.*;

@Controller
public class UserController extends BaseController 
{
	@Autowired
	UserRepository repo;
	
	@Autowired
	FeedbackRepository feedbackRepository;
	
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    @PageTitle(REGISTER)
    public ModelAndView renderRegister(@ModelAttribute(name = MODEL) UserRegisterBindingModel model,
                                       ModelAndView modelAndView) {

        modelAndView.addObject(MODEL, model);

        return view("register", modelAndView);
    }

    @PostMapping("/register")
    public ModelAndView register(@Valid @ModelAttribute(name = MODEL) UserRegisterBindingModel model,
                                 BindingResult bindingResult, ModelAndView modelAndView) {

        if (!model.getPassword().equals(model.getConfirmPassword()) || bindingResult.hasErrors() ||
                this.userService.register(modelMapper.map(model, UserServiceModel.class))==null) {

            modelAndView.addObject(MODEL, model);

            return view("register", modelAndView);
        }
        return redirect("/login");
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    @PageTitle(LOGIN)
    public ModelAndView login(@RequestParam(required = false) String error, ModelAndView modelAndView) {
        if (error != null) {
            modelAndView.addObject(ERROR, "Error");
        }

        return view("/login", modelAndView);
    }


    @GetMapping("/addUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle(REGISTER)
    public ModelAndView renderAddUser(@ModelAttribute(name = MODEL) UserRegisterBindingModel model,
                                       ModelAndView modelAndView) {

        modelAndView.addObject(MODEL, model);

        return view("adduser", modelAndView);
    }

    @PostMapping("/addUser")
    public ModelAndView addUser(@Valid @ModelAttribute(name = MODEL) UserRegisterBindingModel model,
                                 BindingResult bindingResult, ModelAndView modelAndView) {

        if (!model.getPassword().equals(model.getConfirmPassword()) || bindingResult.hasErrors() ||
                this.userService.register(modelMapper.map(model, UserServiceModel.class))==null) {

            modelAndView.addObject(MODEL, model);

            return view("adduser", modelAndView);
        }
        return redirect("/admin/users");
    }
    
    @GetMapping("/user/profile/{username}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle(USER_PROFILE)
    public ModelAndView renderProfilePageByUsername(@PathVariable("username")
                                                                String username, ModelAndView modelAndView) 
    {

        UserServiceModel userServiceModel = this.userService.findByUsername(username);
        //System.out.println(username);
        //if(username=='')

        UsersViewModel usersViewModel = this.modelMapper.map(userServiceModel, UsersViewModel.class);

        modelAndView.addObject(VIEW_MODEL, usersViewModel);

        return view("/profile", modelAndView);
    }
    
    @GetMapping("/admin/profile/{username}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle(USER_PROFILE)
    public ModelAndView renderProfilePageForAdmin(@PathVariable("username")
                                                                String username, ModelAndView modelAndView) 
    {

        UserServiceModel userServiceModel = this.userService.findByUsername(username);
        //System.out.println(username);
        

        UsersViewModel usersViewModel = this.modelMapper.map(userServiceModel, UsersViewModel.class);

        modelAndView.addObject(VIEW_MODEL, usersViewModel);

        return view("/AdminProfile", modelAndView);
    }

    private List<UsersViewModel> mapUserServiceToViewModel(List<UserServiceModel> userServiceModels) 
    {
    	return userServiceModels.stream()
        .map(product -> modelMapper.map(product, UsersViewModel.class))
        .collect(Collectors.toList());
    }
    /*@GetMapping("/admin/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle(USERS)
    public ModelAndView renderAllUsersPage() {

        return view("/users-all");
    }*/
    
    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle(USERS)
    public ModelAndView allUsers(ModelAndView modelAndView) {

        List<UsersViewModel> users =
                mapUserServiceToViewModel(userService.findAllFilteredUsers());

        //modelAndView.addObject(USERS_TO_LOWER_CASE, users);
        modelAndView.addObject(MODEL, users);
        return view("/users-all");
    }

    @PostMapping("/users/edit/role/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView updateUserRole(@PathVariable("id") String id, String role, Principal principal) 
    {
        UserServiceModel currentLoggedUser = this.userService.findByUsername(principal.getName());
        System.out.println(id);
        System.out.println(role);
        UserServiceModel targetUser = userService.findById(id);
        if (role == null){
            return redirect("/user/profile/" + targetUser.getUsername());
        }
        if (currentLoggedUser.getId().equals(id)) {
            return redirect("/user/profile/" + principal.getName());
        }

        try {
            this.userService.updateRole(id, role);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }

        return redirect("/user/profile/" + targetUser.getUsername());
    }
    @GetMapping("/users/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')" + "|| hasRole('ROLE_ADMIN')")
    @PageTitle("Delete User")
    public ModelAndView deleteUser(@PathVariable String id, ModelAndView modelAndView) {
    	System.out.println(id);
        UsersViewModel usersViewModel =
                modelMapper.map(userService.findById(id), UsersViewModel.class);

        modelAndView.addObject(MODEL, usersViewModel);

        return view("/delete-user", modelAndView);
    }
    @PostMapping("/users/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')" + "|| hasRole('ROLE_ADMIN')")
    public ModelAndView deleteUser(@PathVariable String id) {

        userService.deleteUser(id);
        return redirect("/admin/users");
       //return view("/users-all");
    }
    
    @GetMapping("/pusers/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("Delete User")
    public ModelAndView deletePUser(@PathVariable String id, ModelAndView modelAndView) {
    	System.out.println(id);
        UsersViewModel usersViewModel =
                modelMapper.map(userService.findById(id), UsersViewModel.class);

        modelAndView.addObject(MODEL, usersViewModel);

        return view("/delete-puser", modelAndView);
    }
    @PostMapping("/pusers/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deletePUser(@PathVariable String id) {

        userService.deleteUser(id);
        return redirect("/admin/power_users");
       // return view("/users-all");
    }
    

    @GetMapping("/api/users")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UsersViewModel> allUsers() {

        return this.userService.findAllByUsers()
                .stream()
                .map(serviceModel -> this.modelMapper.map(serviceModel, UsersViewModel.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/users/find")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UsersViewModel allUsers(@RequestParam(USERNAME) String username) {

        UserServiceModel byUsername = this.userService.findByUsername(username);

        return byUsername == null ? new UsersViewModel()
                : this.modelMapper.map(byUsername, UsersViewModel.class);
    }

	@GetMapping("/admin/power_users")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle(USERS)
	 public ModelAndView allPowerUsers(ModelAndView modelAndView)  {
		 List<UsersViewModel> users =
	                mapUserServiceToViewModel(userService.findAllFilteredUsers());

	        //modelAndView.addObject(USERS_TO_LOWER_CASE, users);
	        modelAndView.addObject(MODEL, users);
	        return view("/powerusers-all");
       // return view("/powerusers-all");
    }
    
    @GetMapping("/api/powerusers")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UsersViewModel> allPUsers() {

        return this.userService.findAllByPowerUsers()
                .stream()
                .map(serviceModel -> this.modelMapper.map(serviceModel, UsersViewModel.class))
                .collect(Collectors.toList());
    }
    
    @GetMapping("/power_user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle(REGISTER)
	 public ModelAndView renderPuser(@ModelAttribute(name = MODEL) UserRegisterBindingModel model,
	                                       ModelAndView modelAndView)
	 {

	        modelAndView.addObject(MODEL, model);

	        return view("power_user", modelAndView);
	  }
	
	@RequestMapping("/puser")
	//@ResponseBody
	public ModelAndView saveData(User user)
	{
		userService.saveUserWithDefaultRole(user);
		
		repo.save(user);
		//return redirect("/users-all");
		  return redirect("/admin/power_users");
	}
	
	

    @SuppressWarnings("unused")
	private String htmlEscape(String input){
        input = input.replaceAll("&", "&amp;")
              .replaceAll("<", "&lt;")
              .replaceAll(">", "&gt;")
              .replaceAll("\"", "&quot;");

        return input;
    }
    
    //open add-feedback form handler
    @GetMapping("/add-feedback")
    @PageTitle("Add Feedback")
    public String openFeedbackForm(Model model,Principal principal)
    {
    	User currentLoggedUser = this.userService.getByUsername(principal.getName());
    	//System.out.println(currentLoggedUser);
    	
    	model.addAttribute("feedback",new Feedback());
    	model.addAttribute("user",currentLoggedUser);
    	return "addFeedback";
    }
    
    //process feedback handler
    @PostMapping("/process-feedback")
    public String processFeedback(@ModelAttribute Feedback feedback,Principal principal) 
    {
    	User currentLoggedUser = this.userService.getByUsername(principal.getName());
    	//System.out.println(currentLoggedUser);
    	feedback.setCustomer(currentLoggedUser);
    	
    	feedbackRepository.save(feedback);
    	
    	//System.out.println("data = "+feedback);
    	return "redirect:/home";
    }
    
    //show feedbacks handler
    @GetMapping("/all-feedbacks")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Feedbacks")
    public String showFeedbacks(Model model)
    {
    	//send list of feedbacks
    	List<Feedback> feedbacks = this.feedbackRepository.findAll();
    	model.addAttribute("feedbacks", feedbacks);
    	return "allFeedbacks";
    }
    
    //delete feedback handler
    @GetMapping("/feedbacks/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteFeedback(@PathVariable("id") String id,Model model)
    {
    	Optional<Feedback> feedbackOptional = this.feedbackRepository.findById(id);
    	Feedback feedback = feedbackOptional.get();
    	
    	this.feedbackRepository.delete(feedback);
    	return "redirect:/all-feedbacks";
    }
}