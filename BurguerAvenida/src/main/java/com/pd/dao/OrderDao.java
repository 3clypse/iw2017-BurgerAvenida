package com.pd.dao;

import org.springframework.data.repository.CrudRepository;

import com.pd.model.Order;

public interface OrderDao extends CrudRepository<Order, Integer> {
	//List<Order> findByStatusStartsWithIgnoreCase(OrderStatus status);
}
