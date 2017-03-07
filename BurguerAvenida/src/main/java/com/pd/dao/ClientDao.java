package com.pd.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pd.model.Client;

@Repository
public interface ClientDao extends CrudRepository<Client, Integer> {

}
