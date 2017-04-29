package com.pd.service.security;

import java.util.List;
import java.util.Set;

import com.pd.dto.UserListDto;
import com.pd.dto.UserPostDto;
import com.pd.exception.UserAlreadyExistsException;
import com.pd.model.security.RoleName;
import com.pd.model.security.User;

//@PreAuthorize("hasRole('ROLE_ATTENDANT')")
public interface UserService {

	public User create(UserPostDto userPostDto, Set<RoleName> roles) throws UserAlreadyExistsException;
	
	public void delete(Integer id);
	
	public void delete(String username);
	
	public List<UserListDto> readAll();
	
	public User read(Integer id);
	
	public User read(String username);
	
}
