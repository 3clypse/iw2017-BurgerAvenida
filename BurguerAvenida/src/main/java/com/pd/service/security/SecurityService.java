package com.pd.service.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.pd.model.security.RoleName;

public interface SecurityService {
	
    public String findLoggedInUsername();
    
    public Boolean isLoggedIn();

    public Boolean autologin(String username, String password);
    
    public Boolean hasRole(RoleName role);
    
    public Collection<? extends GrantedAuthority> roles();
    
    public void logout();
	
}
