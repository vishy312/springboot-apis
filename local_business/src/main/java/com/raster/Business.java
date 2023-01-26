package com.raster;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Business {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_gen")
	@SequenceGenerator(name="seq_gen", allocationSize=5)
	private Long id;
	
	private String name;
	private String category;
	private String email;
	private String address;
	
	public Business(Long id, String name, String category, String email, String address) {
		
		this.id = id;
		this.name = name;
		this.category = category;
		this.email = email;
		this.address = address;
	}
	
	public Business() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public void validate() {
		if(this.name == null || this.name.isEmpty()) {
			throw new IllegalArgumentException("Name Field can not be empty or null");
		}
		
		if(this.category == null || this.category.isEmpty()) {
			throw new IllegalArgumentException("Category Field can not be empty or null");
		}
		
		if(this.email == null || this.email.isEmpty()) {
			throw new IllegalArgumentException("Email Field can not be empty or null");
		}
		
		if(this.address == null || this.address.isEmpty()) {
			throw new IllegalArgumentException("Address Field can not be empty or null");
		}
		
		String[] categories = {"food", "sports", "Clothing", "grocery", "shoes"};
		boolean present = false;
		for(String cat: categories) {
			if(cat == category) {
				present = true;
			}
		}
		
		if(!present) {
			throw new IllegalArgumentException("Category Field is not valid");
		}
	}
}
