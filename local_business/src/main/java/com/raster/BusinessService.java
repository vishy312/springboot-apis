package com.raster;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ReflectionUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;





@Service
public class BusinessService {
	
	@Autowired
	private BusinessRepository businessRepository;

	public Page<Business> getShops(Pageable pageable) {
		return businessRepository.findAll(pageable);
	}

	public Business getShopById(Long id) {
		Optional<Business> shop = businessRepository.findById(id);
		if(!shop.isPresent()) {
			throw new EntityNotFoundException("A Shop with this id does not exist");
		}
		
		return shop.get();
	}

	public Page<Business> getShopsByKeyword(String key, Pageable pageable) {
		return businessRepository.findByNameContains(key, pageable);
	}

	public Page<Business> getShopsByCategory(String category, Pageable pageable) {
		if(!checkCategory(category)) {
			throw new IllegalArgumentException("This category is not valid");
		}
		
		return businessRepository.findByCategory(category, pageable);
	}

	public void addShop(Business shop) {
		shop.validate();
		businessRepository.save(shop);
	}

	@Transactional
	public void addShops(List<Business> shops) {
		if(shops.size() == 0) {
			throw new NullPointerException("There is no shop present in the Request Body");
		}
		
		int batchSize = 10;
		for(int i = 0; i < shops.size(); i += batchSize) {
			List<Business> batch = shops.subList(i, i + batchSize > shops.size() ? shops.size() : i + batchSize);
			batch.forEach(shop -> shop.validate());
			businessRepository.saveAll(batch);
		}
	}

	public void editShop(Long id, Business shop) {
		shop.validate();
		Optional<Business> shopToUpdate= businessRepository.findById(id);
		if(!shopToUpdate.isPresent()) {
			throw new IllegalArgumentException("No shop found with this id");
		}
		
		Business oldShop = shopToUpdate.get();
		oldShop.setName(shop.getName());
		oldShop.setAddress(shop.getAddress());
		oldShop.setCategory(shop.getCategory());
		oldShop.setEmail(shop.getEmail());
		
		businessRepository.save(shop);
	}

	public void patchShop(Long id, Map<String, String> updates) {
		Optional<Business> shop = businessRepository.findById(id);
		if(!shop.isPresent()) {
			throw new IllegalArgumentException("No shop found with this id");
		}
		
		Business oldShop = shop.get();
		

		
		updates.forEach((key, value) -> {
			switch(key) {
			case "name":
				oldShop.setName(value);
				break;
			case "category":
				if(!checkCategory(key)) {
					throw new IllegalArgumentException("This category is not valid");
				}
				oldShop.setCategory(value);
				break;
			case "email":
				oldShop.setEmail(value);
				break;
			case "address":
				oldShop.setAddress(value);
				break;
			
			}
		});
		businessRepository.save(shop.get());
	}

	public void removeShop(Long id) {
		Optional<Business> shop = businessRepository.findById(id);
		
		if(!shop.isPresent()) {
			throw new IllegalArgumentException("No shop found with this id");
		}
		businessRepository.deleteById(id);
	}
	
	public boolean checkCategory(String category) {
		String[] categories = {"food", "sports", "Clothing", "grocery", "shoes"};
		boolean present = false;
		for(String cat: categories) {
			if(cat == category) {
				present = true;
			}
		}
		
		return present;
	}
}
