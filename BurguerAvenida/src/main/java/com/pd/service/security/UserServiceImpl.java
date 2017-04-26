package com.pd.service.security;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pd.dao.security.RoleDao;
import com.pd.dao.security.UserDao;
import com.pd.dto.UserListDto;
import com.pd.dto.UserPostDto;
import com.pd.exception.UserAlreadyExistsException;
import com.pd.mapping.UserMapper;
import com.pd.model.security.Role;
import com.pd.model.security.RoleName;
import com.pd.model.security.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
    private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Override
	public User create(UserPostDto userPostDto, Set<RoleName> roles) throws UserAlreadyExistsException {
		User user = userDao.findByUsername(userPostDto.getUsername());
		if(user != null) throw new UserAlreadyExistsException();
		else {
			user = userMapper.userPostDtoToEntity(userPostDto);
			user.setEnabled(true);
			user.setPassword(bcrypt.encode(userPostDto.getPassword()));
			Set<Role> rolesSet = new HashSet<Role>();
			for (Iterator<RoleName> it = roles.iterator(); it.hasNext();) {
				Role rol = roleDao.findByName(it.next());
				if(rol != null)
					rolesSet.add(rol);
			}
			user.setRoles(rolesSet);
			return userDao.save(user);
		}
	}
	
	@Override
	public void delete(Integer id) {
		User user = userDao.findOne(id);
		if(user != null)
			user.setEnabled(false);
	}

	@Override
	public void delete(String username) {
		User user = userDao.findByUsername(username);
		if(user != null)
			user.setEnabled(false);
	}

	@Override
	public List<UserListDto> readAll() {
		return StreamSupport
				.stream(userDao.findAll().spliterator(), false)
				.filter(user-> user.isEnabled() == true)
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
