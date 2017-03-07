package com.pd.service;

import java.util.List;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pd.dao.IngredientDao;
import com.pd.dao.ProductDao;
import com.pd.model.Product;
import com.pd.model.ProductType;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private IngredientDao ingredientDao;
	
	@Override
	public Product create() {
		Product product = new Product();
		product.setCost(new Double(10));
		product.setName("Hamburguer Simple");
		product.setIngredients(Lists.newArrayList(ingredientDao.findAll()));
		product.setProductType(ProductType.HAMBURGUER);
		return productDao.save(product);
	}

	@Override
	public Product read(Integer id) {
		return productDao.findOne(id);
	}

	@Override
	public List<Product> readAll() {
		return Lists.newArrayList(productDao.findAll());
	}

	@Override
	public void delete(Integer id) {
		productDao.delete(id);
	}

}
