package com.pd;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pd.service.security.SecurityService;
import com.pd.service.security.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BurguerAvenidaApplicationTests {
	
	@Autowired
	UserService userService;
	
	@Autowired
	SecurityService securityService;
	
	@Test
	public void contextLoads() {
		
	}
	
	@Ignore
	public void testProduct() {
		
	
	}

}
