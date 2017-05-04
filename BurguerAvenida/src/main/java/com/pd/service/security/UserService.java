package com.pd.service.security;

import java.util.List;

import com.pd.dto.UserListDto;
import com.pd.model.security.User;

public interface UserService {

	public User save(User userToCreateOrUpdate);
	
	public void delete(Integer id);
	
	public void delete(String username);
	
	public List<UserListDto> readAll();
	
	public User read(Integer id);
	
	public User read(String username);
	
}
