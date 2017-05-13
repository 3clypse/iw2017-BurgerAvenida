package com.pd.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pd.model.Product;
import com.pd.model.ProductComposite;

@Repository
public interface ProductCompositeDao extends CrudRepository<ProductComposite, Integer> {

	Product findByName(String name);
	List<ProductComposite> findByNameStartsWithIgnoreCase(String name);
	
}
