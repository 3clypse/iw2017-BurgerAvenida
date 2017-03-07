package com.pd.service;

import java.util.List;

import com.pd.model.Product;

public interface ProductService {

	Product create();
	Product read(Integer id);
	List<Product> readAll();
	
}
