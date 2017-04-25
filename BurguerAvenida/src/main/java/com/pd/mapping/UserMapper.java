package com.pd.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.pd.dto.UserPostDto;
import com.pd.model.security.User;

@Mapper(componentModel = "spring", uses=User.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
	
	public UserPostDto toDTO(User user);

	public User toEntity(UserPostDto UserPostDto);

	public void mapToEntity(UserPostDto userDto, @MappingTarget User user);
	
}