package com.pd.service;

import com.pd.model.Restaurant;
import com.pd.model.security.User;

public interface RestaurantService {
	
	Restaurant create(String name, String address, User attendant);
	Restaurant create(String name, String address);
	Restaurant findByName(String name);
	
}
