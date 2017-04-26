package com.pd.service.security;

import java.util.List;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;

import com.pd.dto.UserListDto;
import com.pd.dto.UserPostDto;
import com.pd.exception.UserAlreadyExistsException;
import com.pd.model.security.RoleName;
import com.pd.model.security.User;

public interface UserService {

	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public User create(UserPostDto userPostDto, Set<RoleName> roles) throws UserAlreadyExistsException;
	
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public void delete(Integer id);
	
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public void delete(String username);
	
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public List<UserListDto> readAll();
	
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public User read(Integer id);
	
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public User read(String username);
	
}
