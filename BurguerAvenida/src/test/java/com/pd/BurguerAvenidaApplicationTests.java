package com.pd;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pd.dto.UserPostDto;
import com.pd.exception.UserAlreadyExistsException;
import com.pd.service.security.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BurguerAvenidaApplicationTests {
	
	@Autowired
	UserService userService;
	
	@Test
	public void contextLoads() {
		
	}
	
	@Ignore
	public void testProduct() {
		UserPostDto userPostDto = new UserPostDto("darkborrego", "pass", "Pablo", "Borrego", "dark@borrego.com");
		try {
			userService.create(userPostDto);
		} catch (UserAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
