package com.example.grocerystore.web.controllers;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
//import com.example.grocerystore.configuration;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.grocerystore.configuration.Utility;
import com.example.grocerystore.domain.entities.User;
import com.example.grocerystore.service.UserService;

import net.bytebuddy.utility.RandomString;
	 
	@Controller
	public class ForgotPasswordController {
	    @Autowired
	    private JavaMailSender mailSender;
	     
	    @Autowired
	    private UserService userService;
	     
	    /*@GetMapping("/forgot_password")
	    public String showForgotPasswordForm() {
	 
	    }*/
	    @GetMapping("/forgot_password")
	    public String showForgotPasswordForm() {
	        return "/forgot_password_form";
	    }
	 
	    @PostMapping("/forgot_password")
	    public String processForgotPassword(HttpServletRequest request, Model model) {
	        String email = request.getParameter("email");
	        System.out.println(email);
	        String token = RandomString.make(30);
	         
	        try {
	            userService.updateResetPasswordToken(token, email);
	            //generate reset send mail password link
	            
	            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
	            sendEmail(email, resetPasswordLink);
	            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
	             
	        } catch (UsernameNotFoundException ex) {
	            model.addAttribute("error", ex.getMessage());
	        } catch (UnsupportedEncodingException | MessagingException e) {
	            model.addAttribute("error", "Error while sending email");
	        }
	             
	        return "forgot_password_form";
	    }
	     
	    public void sendEmail(String recipientEmail, String link)
	            throws MessagingException, UnsupportedEncodingException {
	        MimeMessage message = mailSender.createMimeMessage();              
	        MimeMessageHelper helper = new MimeMessageHelper(message);
	         
	        helper.setFrom("priyankaarerao2000@gmail.com", "Grocery Shop");
	        helper.setTo(recipientEmail);
	         
	        String subject = "Here's the link to reset your password";
	         
	        String content = "<p>Hello,</p>"
	                + "<p>You have requested to reset your password.</p>"
	                + "<p>Click the link below to change your password:</p>"
	                + "<p><a href=\"" + link + "\">Change my password</a></p>"
	                + "<br>"
	                + "<p>Ignore this email if you do remember your password, "
	                + "or you have not made the request.</p>";
	         
	        helper.setSubject(subject);
	         
	        helper.setText(content, true);
	         
	        mailSender.send(message);
	    }
	     
	     
	    @GetMapping("/reset_password")
	    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
	        User user = userService.getByResetPasswordToken(token);
	        model.addAttribute("token", token);
	         
	        if (user == null) {
	            model.addAttribute("message", "Invalid Token");
	           // return "message";
	        }
	         
	        return "reset_password_form";
	    }
	    
	   
	    @PostMapping("/reset_password")
	   //@ResponseBody
	    public String processResetPassword(HttpServletRequest request, Model model) {
	        String token = request.getParameter("token");
	        String password = request.getParameter("password");
	        System.out.println(token);
	         
	        User user = userService.getByResetPasswordToken(token);
	        model.addAttribute("title", "Reset your password");
	         
	        if (user == null) {
	            model.addAttribute("message", "Invalid Token");
	            return "message";	
	        } else {           
	            userService.updatePassword(user, password);
	             
	            model.addAttribute("message", "Congratulations You have successfully changed your password.");
	            
	        }
	         
	        //return "message";
	        return "success";
	    }
}
