package com.pd.dao.security;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pd.model.security.RoleName;
import com.pd.model.security.User;

@Repository
public interface UserDao extends CrudRepository<User, Integer>{
	
	@Cacheable
	User findByUsername(String username);
	
	List<User> findByUsernameStartsWithIgnoreCase(String username);
	
	@Query("SELECT u FROM User u JOIN FETCH u.roles r WHERE r.name IN (:role)")
	List<User> findByRole(@Param("role") RoleName role);
	
}
