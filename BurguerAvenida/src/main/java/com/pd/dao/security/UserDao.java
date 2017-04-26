package com.pd.dao.security;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pd.model.security.User;

@Repository
public interface UserDAO extends CrudRepository<User, Integer>{
	User findByUsername(String username);
}
