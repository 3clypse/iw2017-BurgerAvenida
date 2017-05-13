package com.pd.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pd.model.Client;

@Repository
public interface ClientDao extends CrudRepository<Client, Integer> {
	List<Client> findByNameStartsWithIgnoreCase(String name);
}
