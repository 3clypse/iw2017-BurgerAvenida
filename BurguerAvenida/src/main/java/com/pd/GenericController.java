package com.pd;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.pd.dao.ProductDao;
import com.pd.dto.ProductListDto;
import com.pd.mapping.ProductMapper;
import com.pd.service.security.SecurityService;

@RestController
public class GenericController {
	
	@Autowired
	SecurityService securityService;
	
	@Autowired
	ProductDao productDao;
	
	@Autowired
	ProductMapper productMapper;
	
	@RequestMapping(value = "/products", method = RequestMethod.GET)
    public List<ProductListDto> getProducts() {
		return StreamSupport
				.stream(productDao.findByCanBeSoldAlone().spliterator(), false)
				.map(p-> productMapper.productToListDto(p))
				.collect(Collectors.toList());
	}
	
	@RequestMapping(value = "/{path}", method = RequestMethod.GET)
    public ModelAndView go(@PathVariable(name="path") String path) {
		return new ModelAndView("redirect:/#!" + path);
	}
	
	@RequestMapping(value = "/#!{path}", method = RequestMethod.GET)
    public ModelAndView goWhenInside(@PathVariable(name="path") String path) {
		if(!securityService.isLoggedIn())
			return new ModelAndView("redirect:/#!");
		else
			return new ModelAndView("redirect:/#!" + path);
	}

}
