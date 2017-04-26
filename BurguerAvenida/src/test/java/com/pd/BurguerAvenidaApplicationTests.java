package com.pd;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pd.dto.UserPostDto;
import com.pd.exception.UserAlreadyExistsException;
import com.pd.model.security.RoleName;
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
		
		securityService.autologin("darkborrego", "pass");
		
		UserPostDto userPostDto = new UserPostDto("gerente3", "pass", "Pablo", "Borrego", "dark@borrego.com");
		try {
			
			userService.create(userPostDto, new HashSet<RoleName>(Arrays.asList(RoleName.ROLE_ATTENDANT, RoleName.ROLE_WAITER)));
		} catch (UserAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
