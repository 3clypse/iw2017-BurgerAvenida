package com.pd.dao;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pd.model.Restaurant;
import com.pd.model.Zone;

@Repository
public interface ZoneDao extends CrudRepository<Zone, Integer> {
	
	List<Zone> findByDescriptionStartsWithIgnoreCase(String description);
	
	@Cacheable
	List<Zone> findByRestaurant(Restaurant restaurant);
	
}
