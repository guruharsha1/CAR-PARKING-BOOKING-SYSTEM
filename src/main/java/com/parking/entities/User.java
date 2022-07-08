package com.parking.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="userdata")
public class User {

	public String getVehiclenumber() {
		return vehiclenumber;
	}

	public void setVehiclenumber(String vehiclenumber) {
		this.vehiclenumber = vehiclenumber;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
//	@NotBlank(message = "Name field is required !!")
//	@Size(min = 2,max = 20,message = "min 2 and max 20 characters are allowed !!")
	private String name;//First+La
	@Column(unique = true)
	private String email;
	private String password;
	private String role;
//	private String imageUrl;
//	@Column(length = 500)
//	private String about;
	private String username;
	private String address;
	private String mobilenumber;
	private String vehiclenumber;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobilenumber() {
		return mobilenumber;
	}

	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Contact> contact=new ArrayList<>();
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
//	public String getImageUrl() {
//		return imageUrl;
//	}
//
//	public void setImageUrl(String imageUrl) {
//		this.imageUrl = imageUrl;
//	}
//
//	public String getAbout() {
//		return about;
//	}
//
//	public void setAbout(String about) {
//		this.about = about;
//	}

	public List<Contact> getContacts() {
		return contact;
	}

	public void setContacts(List<Contact> contact) {
		this.contact = contact;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+", contacts=" + contact
				+ "]";
	}
	
	
	
	
	
	
	
	
}
