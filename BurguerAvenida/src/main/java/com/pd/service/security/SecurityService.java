package com.pd.service.security;

public interface SecurityService {
	
    public String findLoggedInUsername();

    public boolean autologin(String username, String password);
    
    public void logout();
	
}
