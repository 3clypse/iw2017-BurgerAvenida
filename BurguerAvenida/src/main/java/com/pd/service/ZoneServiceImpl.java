package com.pd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pd.dao.ZoneDao;
import com.pd.model.Restaurant;
import com.pd.model.Zone;
import com.pd.model.ZoneType;

@Service
public class ZoneServiceImpl implements ZoneService {

	@Autowired
	ZoneDao zoneDao;

	@Override
	public Zone create(ZoneType zoneType, String description, Restaurant restaurant) {
		return zoneDao.save(new Zone(zoneType, description, restaurant));
	}

}
