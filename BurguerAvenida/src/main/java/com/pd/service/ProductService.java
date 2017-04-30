package com.pd.service;

import java.util.List;
import java.util.Set;

import com.pd.exception.ProductAlreadyExistsException;
import com.pd.exception.ProductNotExistsException;
import com.pd.model.IVA;
import com.pd.model.Product;

public interface ProductService {

	public Product create(String name, Double price, IVA iva, Set<Product> products) throws ProductAlreadyExistsException;
	
	public Product create(String name, Double price, IVA iva) throws ProductAlreadyExistsException;
	
	public Product read(Integer id) throws ProductNotExistsException;
	
	public Product read(String name) throws ProductNotExistsException;
	
	public void delete(Integer id) throws ProductNotExistsException;
	
	public void delete(String name) throws ProductNotExistsException;
	
	public List<Product> readAll();
	
}
