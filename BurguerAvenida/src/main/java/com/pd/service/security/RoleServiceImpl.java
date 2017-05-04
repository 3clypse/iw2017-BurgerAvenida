package com.pd.service.security;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pd.dao.security.RoleDao;
import com.pd.model.security.Role;
import com.pd.model.security.RoleName;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleDao roleDao;
	
	@Override
	public Set<RoleName> getRoleNames() {
		List<Role> roles = (List<Role>) roleDao.findAll();
		Set<RoleName> roleNames = new HashSet<RoleName>();
		roles.forEach(role->roleNames.add(role.getName()));
		roleNames.removeIf(role->role.equals(RoleName.ROLE_MANAGER));
		return roleNames;
	}

	@Override
	public Role create(RoleName roleName) {
		return roleDao.save(new Role(roleName));
	}

	@Override
	public Set<Role> findAll() {
		List<Role> roles = (List<Role>) roleDao.findAll();
		Set<Role> roleSet = new HashSet<Role>();
		roles.forEach(role->roleSet.add(role));
		return roleSet;
	}

}
