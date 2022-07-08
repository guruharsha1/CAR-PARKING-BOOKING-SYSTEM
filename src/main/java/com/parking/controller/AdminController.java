package com.parking.controller;
 
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
 
import javax.servlet.http.HttpSession;
import com.razorpay.*;
 
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
 
import com.parking.dao.ContactRepository;
import com.parking.dao.UserRepo;
import com.parking.dao.UserRepository;
import com.parking.entities.Contact;
import com.parking.entities.User;
import com.parking.helper.Message;
 
@Controller
@RequestMapping("/admin")
public class AdminController {
 
	private static final String UserRepo = null;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
 
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ContactRepository contactRepository;
 
	// method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println("USERNAME " + userName);
 
		// get the user using usernamne(Email)
 
		User user = userRepository.getUserByUserName(userName);
		System.out.println("USER " + user);
		model.addAttribute("user", user);
 
	}
 
	// dashboard home
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "Admin Dashboard");
		return "normal/admin_dashboard";
	}
 
	// open add form handler
	@GetMapping("/add-slot")
	public String openAddContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
 
		return "normal/add_contact_form";
	}
 
	// processing add contact form
	@PostMapping("/process-slot")
	public String processContact(@ModelAttribute Contact contact,Principal principal, HttpSession session) {
 
		try {
 
			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);
 
			user.getContacts().add(contact);
 
			contact.setUser(user);
 
			this.userRepository.save(user);
 
			System.out.println("DATA " + contact);
 
			System.out.println("Added to data base");
 
			// message success.......
			session.setAttribute("message", new Message("Your contact is added !! Add more..", "success"));
 
		} catch (Exception e) {
			System.out.println("ERROR " + e.getMessage());
			e.printStackTrace();
			// message error
			session.setAttribute("message", new Message("Some went wrong !! Try again..", "danger"));
 
		}
 
		return "normal/add_contact_form";
	}
 
	
	
	@GetMapping("/show-workers/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model m, Principal principal) {
		m.addAttribute("title", "Show User Contacts");
		
 
		String userName = principal.getName();
 
		User user = this.userRepository.getUserByUserName(userName);
 
		Pageable pageable = PageRequest.of(page, 4);
 
		Page<Contact> contact = this.contactRepository.findContactsByUser(user.getId(), pageable);
 
		m.addAttribute("contacts", contact);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", contact.getTotalPages());
 
		return "normal/show_contacts";
	}
	@GetMapping("/show-users/{page}")
	public String showusers(@PathVariable("page") Integer page, Model m, Principal principal) {
		m.addAttribute("title", "Show Users");
		
 
	
		Pageable pageable = PageRequest.of(page, 4);
 
		Page<User> user = this.userRepository.findAll(pageable);
			System.out.println(user);
		m.addAttribute("contacts", user);
		m.addAttribute("currentPage", page);
		 
		m.addAttribute("totalPages", user.getTotalPages());
 
		return "normal/show_users";
	}
	
	// showing particular contact details.
 
	@RequestMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId") Integer cId, Model model, Principal principal) {
		System.out.println("CID " + cId);
 
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();
 
		//
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
 
		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
		//	model.addAttribute("title", contact.getName());
		}
 
		return "normal/contact_detail";
	}
 
	// delete contact handler
 
	@GetMapping("/delete/{cid}")
	@Transactional
	public String deleteContact(@PathVariable("cid") Integer cId, Model model, HttpSession session,
								Principal principal) {
		System.out.println("CID " + cId);
 
		Contact contact = this.contactRepository.findById(cId).get();
		// check...Assignment..image delete
 
		// delete old photo
		String name = principal.getName();
	User user=	this.userRepo.getUserByUserNam(name);
	
		model.addAttribute("user", user);
		user.getContacts().remove(contact);
 
		this.userRepository.save(user);
 
		System.out.println("DELETED");
		session.setAttribute("message", new Message("Contact deleted succesfully...", "success"));
 
		return "redirect:/admin/show-contacts/0";
	}
	
	// delete for users
	
 
 
 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
 
	// open update form handler
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid, Model m) {
 
		m.addAttribute("title", "Update Contact");
 
		Contact contact = this.contactRepository.findById(cid).get();
 
		m.addAttribute("contact", contact);
 
		return "normal/update_form";
	}
 
	// update contact handler
	@RequestMapping(value = "/process-update", method = RequestMethod.POST)
	public String updateHandler(@ModelAttribute Contact contact, Model m, HttpSession session, Principal principal) {
 
		try {
 
			// old contact details
			Contact oldcontactDetail = this.contactRepository.findById(contact.getcId()).get();
 
//			// image..
//			if (!file.isEmpty()) {
//				// file work..
//				// rewrite
//
////				delete old photo
//
//				File deleteFile = new ClassPathResource("static/img").getFile();
//				//File file1 = new File(deleteFile, oldcontactDetail.getImage());
//	//			file1.delete();
//
////				update new photo
//
//				File saveFile = new ClassPathResource("static/img").getFile();
//
//				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
//
//				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//
//			//	contact.setImage(file.getOriginalFilename());
//
//			} else {
//				//contact.setImage(oldcontactDetail.getImage());
//			}
 
			User user = this.userRepository.getUserByUserName(principal.getName());
 
			contact.setUser(user);
 
			this.contactRepository.save(contact);
 
			session.setAttribute("message", new Message("Your contact is updated...", "success"));
 
		} catch (Exception e) {
			e.printStackTrace();
		}
 
		//System.out.println("CONTACT NAME " + contact.getName());
		System.out.println("CONTACT ID " + contact.getcId());
		return "redirect:/user/" + contact.getcId() + "/contact";
	}
 
	// your profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model) {
		model.addAttribute("title", "Profile Page");
		return "normal/profile";
	}
 
	// open settings handler
	@GetMapping("/settings")
	public String openSettings() {
		return "normal/settings";
	}
 
	// change password..handler
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
								 @RequestParam("newPassword") String newPassword, Principal principal, HttpSession session) {
		System.out.println("OLD PASSWORD " + oldPassword);
		System.out.println("NEW PASSWORD " + newPassword);
 
		String userName = principal.getName();
		User currentUser = this.userRepository.getUserByUserName(userName);
		System.out.println(currentUser.getPassword());
 
		if (this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
			// change the password
 
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(currentUser);
			session.setAttribute("message", new Message("Your password is successfully changed..", "success"));
 
		} else {
			// error...
			session.setAttribute("message", new Message("Please Enter correct old password !!", "danger"));
			return "redirect:/user/settings";
		}
 
		return "redirect:/user/index";
	}
 
 
	//creating order for payment
 
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