package com.pd.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Zone implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2983266118391324657L;

	@Id @GeneratedValue(strategy = GenerationType.AUTO) 
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	private ZoneType type;
	
	@ManyToOne
	private Restaurant restaurant;
	
	private String description;

	public Zone() {
		super();
	}

	public Zone(ZoneType type, String description, Restaurant restaurant) {
		super();
		this.type = type;
		this.description = description;
		this.restaurant = restaurant;
	}

	public ZoneType getType() {
		return type;
	}

	public void setType(ZoneType type) {
		this.type = type;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public Integer getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return getDescription();
	}
	
}
