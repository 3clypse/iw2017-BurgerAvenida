package com.pd.dao.security;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pd.model.security.Role;
import com.pd.model.security.RoleName;

@Repository
public interface RoleDAO extends CrudRepository<Role, Long> {
	Role findByName(RoleName name);
}
