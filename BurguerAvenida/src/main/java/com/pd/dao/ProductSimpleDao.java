package com.pd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pd.model.Product;
import com.pd.model.ProductSimple;

@Repository
public interface ProductSimpleDao extends CrudRepository<ProductSimple, Integer> {

	Product findByName(String name);
	List<ProductSimple> findByNameStartsWithIgnoreCase(String name);
	@Query("SELECT p FROM ProductSimple p WHERE p.canBeSoldAlone = true")
	List<ProductSimple> findByCanBeSoldAlone();
}
