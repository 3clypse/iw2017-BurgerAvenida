package com.pd.service;

import java.util.List;
import java.util.Set;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pd.dao.ProductDao;
import com.pd.exception.ProductAlreadyExistsException;
import com.pd.exception.ProductNotExistsException;
import com.pd.model.Product;
import com.pd.model.ProductComposite;
import com.pd.model.ProductSimple;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;
	
	@Override
	public Product create(String name, Double price, Set<Product> products) throws ProductAlreadyExistsException {
		Product product = productDao.findByName(name);
		if(product != null)
			throw new ProductAlreadyExistsException();
		else {
			product = new ProductComposite(name, price, products);
			return productDao.save(product);
		}
	}

	@Override
	public Product create(String name, Double price) throws ProductAlreadyExistsException {
		Product product = productDao.findByName(name);
		if(product != null)
			throw new ProductAlreadyExistsException();
		else {
			product = new ProductSimple(name, price);
			return productDao.save(product);
		}
	}

	@Override
	public Product read(Integer id) throws ProductNotExistsException {
		Product product = productDao.findOne(id);
		if(product != null)
			return product;
		else
			throw new ProductNotExistsException();
	}

	@Override
	public Product read(String name) throws ProductNotExistsException {
		Product product = productDao.findByName(name);
		if(product != null)
			return product;
		else
			throw new ProductNotExistsException();
	}

	@Override
	public void delete(Integer id) throws ProductNotExistsException {
		Product product = productDao.findOne(id);
		if(product != null)
			productDao.delete(product.getId());
		else
			throw new ProductNotExistsException();
	}

	@Override
	public void delete(String name) throws ProductNotExistsException {
		Product product = productDao.findByName(name);
		if(product != null)
			productDao.delete(product.getId());
		else
			throw new ProductNotExistsException();
	}

	@Override
	public List<Product> readAll() {
		return Lists.newArrayList(productDao.findAll());
	}

}
