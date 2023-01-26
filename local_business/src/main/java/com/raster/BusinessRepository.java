package com.raster;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface BusinessRepository extends CrudRepository<Business, Long>, JpaRepository<Business, Long>, PagingAndSortingRepository<Business, Long> {
	
	Page<Business> findByNameContains(String key, Pageable pageable);
	
	Page<Business> findByCategory(String category, Pageable pageable);

//	void save(Business shop, FlushModeType flushMode);
}
