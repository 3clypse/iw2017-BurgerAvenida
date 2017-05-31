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
		if(securityService.hasRole(RoleName.ROLE_MANAGER))
			return true;
		else if (beanName.equals("updateOrderView") ||
				beanName.equals("closeOrderView") ||
				beanName.equals("generateTicketView") ||
				beanName.equals("newOrderView"))
			return securityService.hasRole(RoleName.ROLE_WAITER) || securityService.hasRole(RoleName.ROLE_ATTENDANT);
		else if (beanName.equals("clientView") ||
			 beanName.equals("orderView") ||
			 beanName.equals("productSimpleView") ||
			 beanName.equals("productCompositeView") ||
			 beanName.equals("productFamilyView") ||
			 beanName.equals("restaurantView") ||
			 beanName.equals("userView") ||
			 beanName.equals("zoneView")) 
			return securityService.hasRole(RoleName.ROLE_MANAGER);
		else if (beanName.equals("checkoutView"))
			return securityService.hasRole(RoleName.ROLE_ATTENDANT);
		else return true;
	}
	
}