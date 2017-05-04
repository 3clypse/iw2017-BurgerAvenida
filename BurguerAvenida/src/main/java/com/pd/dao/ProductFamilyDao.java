package com.pd.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.pd.model.ProductFamily;

public interface ProductFamilyDao extends CrudRepository<ProductFamily, Integer> {
	
	List<ProductFamily> findByNameStartsWithIgnoreCase(String name);
}
