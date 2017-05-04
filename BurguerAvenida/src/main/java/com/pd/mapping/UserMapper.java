package com.pd.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.pd.dto.UserListDto;
import com.pd.dto.UserPostDto;
import com.pd.model.security.User;

@Mapper(componentModel = "spring", uses=User.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
	
	public UserPostDto userToPostDto(User user);
	
	public UserListDto userToListDto(User user);
	
	public User userListDtoToEntity(UserListDto userListDto);
	
	public User userToEntity(User user);

	public User userPostDtoToEntity(UserPostDto UserPostDto);

}