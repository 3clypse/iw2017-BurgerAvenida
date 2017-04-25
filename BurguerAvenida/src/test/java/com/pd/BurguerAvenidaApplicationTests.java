package com.pd;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pd.dao.ClientDao;
import com.pd.dao.IngredientDao;
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
		
	}

}
