package com.parking.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.parking.dao.UserRepo;
import com.parking.entities.User;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
@Controller
@RequestMapping("/user")
public class UserController {
@Autowired
private UserRepo userRepo;

@ModelAttribute
public void addCommon(Model model, Principal principal)
{String userName = principal.getName();
System.out.println("USERNAME " + userName);
	String name = principal.getName();

	User user=this.userRepo.getUserByUserNam(name);
	model.addAttribute("user", user);}
@RequestMapping("/index")
public String Homeb(Model model, Principal principal) {
	
	

	return "normal1/user_dashboard";
}
@RequestMapping("/addslo")
public String addSlo(Model model, Principal principal) {
	return "normal1/add_slot";
}
@RequestMapping("/profile1")
public String pro(Model model, Principal principal) {
	return "normal1/profile1";
}
@RequestMapping("/viewslot")
public String viewslot(Model model, Principal principal) {
	return "normal1/view_slot";
}
@RequestMapping("/wallet")
public String wallet(Model model, Principal principal) {
	return "normal1/wallet";
}
@RequestMapping("/carmodel")
public String carmodel(Model model, Principal principal) {
	return "normal1/carmodel";
}
@RequestMapping("/book-a-slot")
public String BookingSlots(@RequestParam("no_of_hours") int noh,
		
		@RequestParam("car_washing") String car_washing
		,@RequestParam("air_filling")String air_filling
		,@RequestParam("car_model") String car_model,
		@RequestParam("slot_number") int  slot_number,Model model
		,HttpSession session)
		 {
	
	
	Date t = new Date();
	SimpleDateFormat ty1 = new SimpleDateFormat("hh:mm:ss");
	String in_time = ty1.format(t);
	
	int cost_parking=0;
	cost_parking=noh*25;
	
	if(car_washing.equals("YES") && air_filling.equals("YES")) {
		cost_parking=cost_parking+250;
	}
	else if( air_filling.equals("YES")) {
		cost_parking=cost_parking+50;
	}
	else if(car_washing.equals("YES") ) {
		cost_parking=cost_parking+200;
	}
	String s;
	if(car_model.equals("SUV")) {
	  s="Your most suitable parking slot is between 80 to 100";
	}
	else if(car_model.equals("SEDAN")) {
		s ="Your most suitable parking slot is between 50 to 70";
	}
	else {
		s ="Your most suitable parking slot is between 1 to 50";
	}
	
	model.addAttribute("intime", in_time);
	model.addAttribute("hour", noh);
	model.addAttribute("car_model", car_model);
	model.addAttribute("preference", s);
	model.addAttribute("index", slot_number);
	model.addAttribute("amount",cost_parking);
		session.setAttribute("cost_parking",cost_parking);
		
	return "normal1/display";
}
@RequestMapping("/paying")
public String remaingMoney(HttpSession session,Model model) {
	int amount=(int)session.getAttribute("cost_parking");
	int a=500-amount;
model.addAttribute("money_to_body",a);

			return "normal1/new";
	
}
@RequestMapping("/model_car")
public String modelcar(Model model,@RequestParam("model1")String model1) {
	String sec;
	if(model1.equals("SUV")) {
		  sec="Your most suitable parking slot is between 80 to 100";
		}
		else if(model1.equals("SEDAN")) {
			sec ="Your most suitable parking slot is between 50 to 70";
		}
		else {
			sec ="Your most suitable parking slot is between 1 to 50";
		}
	model.addAttribute("pre", sec);
	return "normal1/new";
}

@PostMapping("/create_order")
@ResponseBody
public String createOrder(@RequestBody Map<String, Object> data) throws Exception
{
	//System.out.println("Hey order function ex.");
	System.out.println(data);
	
	int amt=Integer.parseInt(data.get("amount").toString());
	
	var client=new RazorpayClient("rzp_test_haDRsJIQo9vFPJ", "owKJJes2fwE6YD6DToishFuH");
	
	JSONObject ob=new JSONObject();
	ob.put("amount", amt*100);
	ob.put("currency", "INR");
	ob.put("receipt", "txn_235425");
	
	//creating new order
	
	Order order = client.Orders.create(ob);
	System.out.println(order);
	
	//if you want you can save this to your data..		
	return order.toString();
}

}

