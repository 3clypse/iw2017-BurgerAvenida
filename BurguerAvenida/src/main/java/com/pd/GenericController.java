package com.pd;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class GenericController {

	@RequestMapping(value = "/{path}", method = RequestMethod.GET)
    public ModelAndView go(@PathVariable(name="path") String path) {
		return new ModelAndView("redirect:#!" + path);
	}
	
//	@RequestMapping(method = RequestMethod.GET)
//    public ModelAndView goHome() {
//		return new ModelAndView("redirect:#!");
//	}
	
}
