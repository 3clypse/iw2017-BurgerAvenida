package com.pd.service;

import com.pd.model.Restaurant;
import com.pd.model.Zone;
import com.pd.model.ZoneType;

public interface ZoneService {
	
	Zone create(ZoneType zoneType, String description, Restaurant restaurant);
	
}
