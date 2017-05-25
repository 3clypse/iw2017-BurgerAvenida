package com.pd.dao;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pd.model.Restaurant;
import com.pd.model.security.User;

@Repository
public interface RestaurantDao extends CrudRepository<Restaurant, Integer> {
	
	Restaurant findByName(String name);
	
	List<Restaurant> findByNameStartsWithIgnoreCase(String name);
	
	Restaurant findByAttendant(User attendant);
	
	@Cacheable
	@Query("SELECT DISTINCT res FROM Restaurant res JOIN FETCH res.workers users WHERE :worker IN elements(users)")
	Restaurant findByWorker(@Param("worker") User worker);
}
