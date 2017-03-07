package com.pd;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pd.dao.ClientDao;
import com.pd.dao.IngredientDao;
import com.pd.model.Client;
import com.pd.model.Ingredient;
import com.pd.service.ProductService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BurguerAvenidaApplicationTests {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ClientDao clientDao;
	
	@Autowired
	private IngredientDao ingredientDao;
	
	@Test
	public void contextLoads() {
	}
	
	@Ignore
	public void testProduct() {
		clientDao.save(new Client(1, "Pablo", 622313639, "Calle Alerce 12"));
		ingredientDao.save(new Ingredient(1, "Tomatoe", new Double(1.5)));
		ingredientDao.save(new Ingredient(2, "Cheese", new Double(2)));
		ingredientDao.save(new Ingredient(3, "Meat", new Double(3)));
		
		productService.create();
		productService.readAll().forEach(p->{System.out.println(p);});
	}

}
