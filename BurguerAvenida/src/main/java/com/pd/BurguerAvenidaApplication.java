package com.pd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.pd.dao.ClientDao;
import com.pd.dao.IngredientDao;
import com.pd.model.Client;
import com.pd.model.Ingredient;
import com.pd.service.ProductService;

@SpringBootApplication
public class BurguerAvenidaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BurguerAvenidaApplication.class, args);
	}

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ClientDao clientDao;
	
	@Autowired
	private IngredientDao ingredientDao;
	
	@Override
	public void run(String... args) throws Exception {
		clientDao.save(new Client(1, "Pablo", 622313639, "Calle Alerce 12"));
		ingredientDao.save(new Ingredient(1, "Tomatoe", new Double(1.5)));
		ingredientDao.save(new Ingredient(2, "Cheese", new Double(2)));
		ingredientDao.save(new Ingredient(3, "Meat", new Double(3)));
		
		productService.create();
		productService.readAll().forEach(p->{System.out.println(p);});
	}
}
