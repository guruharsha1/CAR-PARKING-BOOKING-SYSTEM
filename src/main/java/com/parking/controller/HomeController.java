package com.parking.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.parking.dao.UserRepository;
import com.parking.entities.User;
import com.parking.helper.Message;

@Controller
public class HomeController {
	@Autowired
	ForgotController f=new ForgotController();

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/user-home")
	public String userhome(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home1";
	}
	@RequestMapping("/admin-home")
	public String adminhome(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}
	@RequestMapping("/")
	public String intial_PAGE(){
		return "normal/test_user";
		
	}
	@RequestMapping("/userhome")
	public String usehome(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home1";
	}
	@RequestMapping("/signinuser")
	public String signinuser(Model model) {
		model.addAttribute("title", "Login-User");
		return "login1";
	}
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}

	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}

	// handler for registering user
	
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {

		try {

			if (!agreement) {
				System.out.println("You have not agreed the terms and conditions");
				throw new Exception("You have not agreed the terms and conditions");
			}

			if (result1.hasErrors()) {
				System.out.println("ERROR " + result1.toString());
				model.addAttribute("user", user);
				return "signup";
			}

			else {
				
				f.openEmailForm();
				
				user.setRole("ROLE_USER");
//				user.setEnabled(true);
//				user.setImageUrl("default.png");
				user.setPassword(passwordEncoder.encode(user.getPassword()));

				System.out.println("Agreement " + agreement);
				System.out.println("USER " + user);

				User result = this.userRepository.save(user);

				model.addAttribute("user", new User());

				session.setAttribute("message", new Message("Successfully Registered !!", "alert-success"));
				return "signup";
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something Went wrong !! " + e.getMessage(), "alert-danger"));
			return "signup";
		}

	}

	//handler for custom login
	@GetMapping("/signin")
	public String customLogin(Model model)
	{
		model.addAttribute("title","Login Page");
		return "login";
	}
	@GetMapping("/login-failed")
	public String login_failed(Model model)
	{
		model.addAttribute("title","Login Page");
		return "login_failed";
	}
}
