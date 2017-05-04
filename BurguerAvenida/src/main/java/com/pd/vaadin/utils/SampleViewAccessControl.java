package com.pd.vaadin.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pd.model.security.RoleName;
import com.pd.service.security.SecurityService;
import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.ui.UI;

@Component
public class SampleViewAccessControl implements ViewAccessControl {

	@Autowired
	SecurityService securityService;

	@Override
	public boolean isAccessGranted(UI ui, String beanName) {
		//System.out.println("COMPROBANDO " + beanName + " PARA USUARIO CON ROLES: " +securityService.roles());
		if(securityService.hasRole(RoleName.ROLE_MANAGER)){
			return true;
		} else if (beanName.equals("homeView")) {
			return true;
		} else if (beanName.equals("aboutView")){
			return securityService.hasRole(RoleName.ROLE_WAITER) || securityService.hasRole(RoleName.ROLE_ATTENDANT);
		} else if (beanName.equals("userView")){
			return securityService.hasRole(RoleName.ROLE_MANAGER);
		} 
		else return true;
	}
	
}