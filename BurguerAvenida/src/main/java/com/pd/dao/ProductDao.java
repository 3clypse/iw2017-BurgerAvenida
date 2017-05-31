package com.pd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pd.model.Product;

@Repository
public interface ProductDao extends CrudRepository<Product, Integer> {

	Product findByName(String name);
	List<Product> findByNameStartsWithIgnoreCase(String name);
	@Query("SELECT p FROM Product p WHERE p.canBeSoldAlone = true")
	List<Product> findByCanBeSoldAlone();
	
}
