package com.pd;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pd.dao.OrderDao;
import com.pd.dao.OrderLineDao;
import com.pd.dao.ProductDao;
import com.pd.model.Order;
import com.pd.model.OrderLine;
import com.pd.model.Product;
import com.pd.service.security.SecurityService;
import com.pd.service.security.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BurguerAvenidaApplicationTests {
	
	@Autowired
	UserService userService;
	
	@Autowired
	SecurityService securityService;
	
	@Autowired
	OrderDao orderDao;
	
	@Autowired
	ProductDao productDao;
	
	@Autowired
	OrderLineDao orderLineDao;
	
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testVoidOrder() {
		Order order = new Order();
			  order = orderDao.save(order);
		
		Order order2 = orderDao.findOne(order.getId());
		
		Assert.assertEquals(order2.getId(), order.getId());
	}
	
	@Test
	public void testOrderLine(){
		Product p = new Product();
				p.setName("Zumo");
				p.setPrice("1");
		
		Order o = new Order();
		
		OrderLine ol = new OrderLine();
				  ol.setProduct(p);
				  ol.setOrderObject(o);
				  ol.setAmount(5);;
		
		//Test if product and total price is the same.
		Assert.assertEquals(ol.getProduct(), p);
		Assert.assertEquals(ol.getTotal()  , new Double(5));
	}

}
