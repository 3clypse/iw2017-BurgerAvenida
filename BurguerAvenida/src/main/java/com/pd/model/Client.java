package com.pd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Client implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1696132505136810628L;
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(length = 32)
	private String name;
	@Column(length = 9)
	private Integer phoneNumber;
	@Column(length = 64)
	private String address;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
	private List<Order> orders = new ArrayList<Order>();
	
	public Client() {
		super();
	}

	public Client(String name, Integer phoneNumber, String address) {
		super();
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}

	public Integer getId() {
		return id;
	}

	protected void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Integer phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
