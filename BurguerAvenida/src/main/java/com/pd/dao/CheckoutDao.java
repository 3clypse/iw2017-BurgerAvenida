package com.pd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pd.model.Checkout;
import com.pd.model.Restaurant;

@Repository
public interface CheckoutDao extends CrudRepository<Checkout, Integer> {
	
	List<Checkout> findByRestaurant(Restaurant restaurant);
	
	@Query("SELECT c FROM checkout c WHERE SUBSTRING(c.date, 1, 10) = :date")
	Checkout findByDate(@Param("date") String string);
}
