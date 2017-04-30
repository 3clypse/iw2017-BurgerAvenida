package com.pd.service.security;

import java.util.Set;

import com.pd.model.security.Role;
import com.pd.model.security.RoleName;

public interface RoleService {
	
	Role create(RoleName roleName);
	
	Set<RoleName> getRoleNames();
	
	Set<Role> findAll();
	
}
