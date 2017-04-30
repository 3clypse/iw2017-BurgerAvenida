package com.pd.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pd.model.Zone;

@Repository
public interface ZoneDao extends CrudRepository<Zone, Integer> {
	
}
