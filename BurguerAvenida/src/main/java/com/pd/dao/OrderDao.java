package com.pd.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.pd.model.Order;
import com.pd.model.OrderStatus;
import com.pd.model.OrderType;
import com.pd.model.Restaurant;

public interface OrderDao extends CrudRepository<Order, Integer> {
	@Query("SELECT o FROM orders o WHERE o.status = :orderstatus "
			+ "and o.restaurant = :restaurant")
	List<Order> findByStatus(
			@Param("orderstatus") OrderStatus orderstatus, 
			@Param("restaurant") Restaurant restaurant);
	
	@Query("SELECT o FROM orders o WHERE o.status = :orderstatus "
			+ "and o.type = :ordertype and o.restaurant = :restaurant")
	List<Order> findByStatusAndType(
			@Param("orderstatus") OrderStatus orderstatus, 
			@Param("ordertype") OrderType ordertype,
			@Param("restaurant") Restaurant restaurant);
	
	@Query("SELECT DISTINCT SUBSTRING(d.closedAt, 1, 10) "
			+ "FROM orders d WHERE d.status = 'CLOSED'")
    List<String> findAllDates();
	
	@Query("SELECT o FROM orders o "
			+ "WHERE SUBSTRING(o.closedAt, 1, 10) = :date "
			+ "AND o.status = :orderstatus")
	List<Order> findByDate(
			@Param("date") String string, 
			@Param("orderstatus") OrderStatus orderstatus);
}
