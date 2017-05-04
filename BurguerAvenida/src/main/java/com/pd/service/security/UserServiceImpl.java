package com.pd.service.security;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pd.dao.security.UserDao;
import com.pd.dto.UserListDto;
import com.pd.mapping.UserMapper;
import com.pd.model.security.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
    private UserDao userDao;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Override
	public User save(User userToCreateOrUpdate) {
		User user = userDao.findByUsername(userToCreateOrUpdate.getUsername());
		if(user == null) {
			//Create
			user = userMapper.userToEntity(userToCreateOrUpdate);
			user.setPassword(bcrypt.encode(userToCreateOrUpdate.getPassword()));
		}else{
			//Update
			user = userMapper.userToEntity(userToCreateOrUpdate);
			if(!userToCreateOrUpdate.getPassword().startsWith("$2a$10$")) {
			user.setPassword(bcrypt.encode(userToCreateOrUpdate.getPassword()));
			}
		}
		return userDao.save(user);
	}
	
	@Override
	public void delete(Integer id) {
		User user = userDao.findOne(id);
		if(user != null)
			userDao.delete(id);
	}

	@Override
	public void delete(String username) {
		User user = userDao.findByUsername(username);
		if(user != null)
			userDao.delete(user.getId());
	}

	@Override
	public List<UserListDto> readAll() {
		return StreamSupport
				.stream(userDao.findAll().spliterator(), false)
				.map(user-> userMapper.userToListDto(user)).collect(Collectors.toList());
	}

	@Override
	public User read(Integer id) {
		return userDao.findOne(id);
	}

	@Override
	public User read(String username) {
		return userDao.findByUsername(username);
	}

}
