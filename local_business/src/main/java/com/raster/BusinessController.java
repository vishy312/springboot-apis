package com.raster;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins="*")
@RestController
public class BusinessController {
	
	@Autowired
	private BusinessService businessService;
	
	@Async
	@GetMapping(value="/shops", produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public Future<ResponseEntity<Object>> getShops(@Validated @RequestParam(required=false, defaultValue="0")int page){
		
		Pageable pageable = PageRequest.of(page, 10, Sort.by(Direction.ASC, "name"));
		Page<Business> businesses = businessService.getShops(pageable);
		
		if(businesses.isEmpty()) {
			throw new BusinessNotFoundException("No shops found for the given parameters");
		}
		
		if(page < 0) {
			throw new InvalidRequestException("Page number can not be negative");
		}
		
		ResponseEntity<Object> response = new ResponseEntity<>(businesses, HttpStatus.OK);
		
		return new AsyncResult<>(response);
	}
	
	@GetMapping(value="/shops/{id}", produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public Future<ResponseEntity<Object>> getShopById(@PathVariable Long id) {
		Business shop =  businessService.getShopById(id);
		if(shop == null) {
			throw new BusinessNotFoundException("No shop found for the given parameters");
		}
		
		ResponseEntity<Object> response = new ResponseEntity<>(shop, HttpStatus.OK);
		
		return new AsyncResult<>(response);
	}
	
	@GetMapping(value="/shops/keyword", produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public Future<ResponseEntity<Object>> getShopsByKeyword(@Validated @RequestParam(required=false) String key, @RequestParam(required=false, defaultValue="0") int page ){
		Pageable pageable = PageRequest.of(page, 10, Sort.by(Direction.ASC, "name"));
		Page<Business> businesses = businessService.getShopsByKeyword(key, pageable);
		
		if(page < 0) {
			throw new InvalidRequestException("Page number can not be a negative number");
		}
		
		ResponseEntity<Object> response = new ResponseEntity<>(businesses, HttpStatus.OK);
		
		return new AsyncResult<>(response);
	}
	
	@GetMapping(value="/shops/categories/{category}", produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public Future<ResponseEntity<Object>> getShopsByCategory(@Validated @PathVariable String category, @Validated @RequestParam(required=false, defaultValue="0") int page){
		Pageable pageable = PageRequest.of(page, 10, Sort.by(Direction.ASC, "name"));
		Page<Business> businesses = businessService.getShopsByCategory(category, pageable);
		String[] categories = {"food", "sports", "shoes", "Clothing", "grocery"};
		boolean foundCategory = false;
		for(int i = 0; i < categories.length; i++) {
			if(categories[i].equalsIgnoreCase(category)) {
				foundCategory = true;
			}
		}
		
		if(!foundCategory) {
			throw new BusinessNotFoundException("No shops found for the given parameters"); 
		}
		
		if(page < 0) {
			throw new InvalidRequestException("Page number can not be a negative number");
		}
		
		ResponseEntity<Object> response = new ResponseEntity<>(businesses, HttpStatus.OK);
		
		return new AsyncResult<>(response);
	}
	
	@PostMapping("/shop")
	public ResponseEntity<Business> addShop(@Validated @RequestBody Business shop) {
		try {
			businessService.addShop(shop);
			
			return ResponseEntity.ok(shop);
		}catch(PostBusinessException e) {
			throw new PostBusinessException("No new shops added");
		}
	}
	
	@PostMapping("/shops")
	public ResponseEntity<List<Business>> addShops(@Validated @RequestBody List<Business> shops) {
		try {
			if(shops.size() == 1) {
				businessService.addShop(shops.get(0));
			}else if(shops.size() <= 0) {
				throw new InvalidRequestException("Invalid Request");
			}else {
				businessService.addShops(shops);
			}
			
			return ResponseEntity.ok(shops);
		}catch(PostBusinessException ex) {
			throw new PostBusinessException("No new shops added");
		}
	}
	
	@PutMapping("/shops/{id}")
	public ResponseEntity<Business> editShop(@Validated @PathVariable Long id, @Validated @RequestBody Business shop) {
		Business shopToUpdate = businessService.getShopById(id);
		if(shopToUpdate == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		try {
			businessService.editShop(id, shop);
			return ResponseEntity.ok(shop);
		}catch(PutBusinessException ex) {
			throw new PutBusinessException("Shop could not be edited!");
		}
	}
	
	@PatchMapping(value="/shops/{id}", consumes = "application/json")
	public ResponseEntity<Void> patchShop(@Validated @PathVariable Long id, @Validated @RequestBody Map<String, String> updates) {
		Business shopToUpdate = businessService.getShopById(id);
		if(shopToUpdate == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		businessService.patchShop(id, updates);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("/shops/{id}")
	public ResponseEntity<Business> removeShop(@Validated @PathVariable Long id) {
		Business shop = businessService.getShopById(id);
		if(shop == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		businessService.removeShop(id);
		return ResponseEntity.ok(shop);
	}
}
