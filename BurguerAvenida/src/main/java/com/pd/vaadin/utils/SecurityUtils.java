package com.pd.vaadin.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

	public static String getCurrentUser() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
}
