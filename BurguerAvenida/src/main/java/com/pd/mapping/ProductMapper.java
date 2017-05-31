package com.pd.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.pd.dto.ProductListDto;
import com.pd.model.Product;
import com.pd.model.security.User;

@Mapper(componentModel = "spring", uses=User.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
	
	public ProductListDto productToListDto(Product product);
	
}