package com.pd.service.security;

import com.pd.dto.UserPostDto;
import com.pd.exception.UserAlreadyExistsException;
import com.pd.model.security.User;

public interface UserService {

	public User create(UserPostDto userPostDto) throws UserAlreadyExistsException;
	
}
