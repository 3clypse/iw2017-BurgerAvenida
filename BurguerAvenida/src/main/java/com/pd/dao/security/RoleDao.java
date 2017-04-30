package com.pd.dao.security;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pd.model.security.Role;
import com.pd.model.security.RoleName;

@Repository
public interface RoleDao extends CrudRepository<Role, Integer> {
	Role findByName(RoleName name);
}
