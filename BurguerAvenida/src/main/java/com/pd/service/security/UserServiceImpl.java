package com.pd.service.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pd.dao.security.RoleDAO;
import com.pd.dao.security.UserDAO;
import com.pd.dto.UserPostDto;
import com.pd.exception.UserAlreadyExistsException;
import com.pd.mapping.UserMapper;
import com.pd.model.security.RoleName;
import com.pd.model.security.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
    private UserDAO userDAO;
	
	@Autowired
	private RoleDAO roleDAO;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Override
	public User create(UserPostDto userPostDto) throws UserAlreadyExistsException {
		User user = userDAO.findByUsername(userPostDto.getUsername());
		if(user != null) throw new UserAlreadyExistsException();
		else {
			user = userMapper.toEntity(userPostDto);
			user.setEnabled(true);
			user.setPassword(bcrypt.encode(userPostDto.getPassword()));
			user.setRoles(Arrays.asList(roleDAO.findByName(RoleName.ROLE_MANAGER)));
			return userDAO.save(user);
		}
	}
	
}
