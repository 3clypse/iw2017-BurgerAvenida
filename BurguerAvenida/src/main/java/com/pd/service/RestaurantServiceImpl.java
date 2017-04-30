package com.pd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pd.dao.RestaurantDao;
import com.pd.model.Restaurant;
import com.pd.model.security.User;

@Service
public class RestaurantServiceImpl implements RestaurantService {

	@Autowired
	RestaurantDao restaurantDao;
	
	@Override
	public Restaurant create(String name, String address, User attendant) {
		return restaurantDao.save(new Restaurant(name, address, attendant));
	}

	@Override
	public Restaurant create(String name, String address) {
		return restaurantDao.save(new Restaurant(name, address));
	}
	
	@Override
	public Restaurant findByName(String name) {
		return restaurantDao.findByName(name);
	}

}
