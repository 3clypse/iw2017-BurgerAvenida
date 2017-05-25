package com.pd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.pd.service.security.SecurityService;

@RestController
public class GenericController {
	
	@Autowired
	SecurityService securityService;
	
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
