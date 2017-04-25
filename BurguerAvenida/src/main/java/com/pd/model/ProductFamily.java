package com.pd.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ProductFamily implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4978599747978473141L;

	@Id @GeneratedValue(strategy = GenerationType.AUTO) 
	private Integer id;
	
	private String name;

	public ProductFamily() {
		super();
	}
	
	public ProductFamily(String name) {
		super();
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
