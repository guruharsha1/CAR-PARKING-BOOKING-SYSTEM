package com.parking.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.parking.dao.UserRepository;
import com.parking.entities.User;
import com.parking.service.EmailService;

@Controller
public class ForgotController {
	
	Random random = new Random(1000);
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepsitory;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	String to;
	//email id form open  handler
	@RequestMapping("/verification")
	public String openEmailForm()
	{
		return "forgot_email_form";//same as forgot password 
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email,HttpSession session)
	{
		System.out.println("EMAIL "+email);
		
		//generating otp of 4 digit
		
		
		int otp = random.nextInt(999999);
		
		System.out.println("OTP "+otp);
		
		
		//write code for send otp to email...
		
		
		String subject="Registration for Parking Management system";
		String message=""
				+ "<div style='border:1px solid #e2e2e2; padding:20px'>"
				+ "<h1>"
				+ "OTP is "
				+ "<b>"+otp
				+ "</n>"
				+ "</h1> "
				+ "</div>";
		 to=email;
		
		boolean flag = this.emailService.sendEmail(subject, message,to);
		
		if(flag)
		{
			
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			return "verify_otp";
			
		}else
		{	
			
			session.setAttribute("message", "Check your email id !!");
			
			return "forgot_email_form";
		}
		
		
	}
	
	//verify otp
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp,HttpSession session)
	{
		int myOtp=(int)session.getAttribute("myotp");
		
		System.out.println("User OTP "+otp);
		System.out.println("Our OTP "+myOtp);
		
		//String email=(String)session.getAttribute("email");
		if(myOtp==otp)
		{
			session.setAttribute("str",to);
			return "signup";
		}else
		{
			session.setAttribute("message", "You have entered wrong otp !!");
			return "verify_otp";
		}
	}

	
	
}
