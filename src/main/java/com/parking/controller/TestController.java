package com.parking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

	@RequestMapping("/addslot")
	public String method1() {
		return "normal/add_slot";
	}
	@RequestMapping("/user-dashboard")
	public String userDashboard() {
		return "normal/user_dashboard";
	}
	
}
