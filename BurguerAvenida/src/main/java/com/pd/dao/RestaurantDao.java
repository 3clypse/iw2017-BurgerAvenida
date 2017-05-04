package com.pd.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pd.model.Restaurant;

@Repository
public interface RestaurantDao extends CrudRepository<Restaurant, Integer> {
	
	Restaurant findByName(String name);
	List<Restaurant> findByNameStartsWithIgnoreCase(String name);
	
}
