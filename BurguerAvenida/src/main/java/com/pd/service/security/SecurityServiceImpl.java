package com.pd.service.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.pd.model.security.RoleName;

@Service
public class SecurityServiceImpl implements SecurityService {
	
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public String findLoggedInUsername() {
    	Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (userDetails instanceof UserDetails) {
            return ((UserDetails) userDetails).getUsername();
        }
        return null;
    }
    
    @Override
    public Boolean isLoggedIn() {
    	return (findLoggedInUsername() == null) ? false:true;
    }

    @Override
    public Boolean autologin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            return true;
        } else return false;
    }
    
	@Override
	public void logout() {
		if(isLoggedIn()) {
			SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false); 
		}
	}
	
	public Collection<? extends GrantedAuthority> roles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if(authentication != null ){
        	return authentication.getAuthorities();
        } else{
        	return null;
        }
    }
	
	@Override
	public Boolean hasRole(RoleName role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority(role.toString()));
    }

}