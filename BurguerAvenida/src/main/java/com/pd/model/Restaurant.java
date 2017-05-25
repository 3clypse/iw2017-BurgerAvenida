package com.pd.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.pd.model.security.User;

@Entity
public class Restaurant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1302224010276448331L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(length = 32, unique = true)
	private String name;
	@Column(length = 64)
	private String address;

	@OneToOne
	private User attendant;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workin")
	private Set<User> workers = new HashSet<User>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
	private Set<Zone> zones = new HashSet<Zone>();

	public Restaurant() {
		super();
	}

	public Restaurant(String name, String address, User attendant) {
		super();
		this.name = name;
		this.address = address;
		this.attendant = attendant;
	}

	public Restaurant(String name, String address) {
		super();
		this.name = name;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public User getAttendant() {
		return attendant;
	}

	public void setAttendant(User attendant) {
		this.attendant = attendant;
	}

	public Integer getId() {
		return id;
	}

	public Set<Zone> getZones() {
		return zones;
	}

	public void setZones(Set<Zone> zones) {
		this.zones = zones;
	}
	
	public Set<User> getWorkers() {
		return workers;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Restaurant other = (Restaurant) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name + ", " + address;
	}
	
}
