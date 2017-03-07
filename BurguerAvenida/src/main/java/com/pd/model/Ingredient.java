package com.pd.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity(name = "ingredient")
@Table(name = "ingredient")
public class Ingredient implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7968248009825377759L;
	
	public Ingredient() {
		super();
	}
	
	public Ingredient(Integer id, String name, Double cost) {
		super();
		this.id = id;
		this.name = name;
		this.cost = cost;
	}
	
	@Id
	private Integer id;
	@NotNull @Column(length = 32, unique = true) 
	private String name;
	@NotNull @Column(length = 8, precision = 2)
	private Double cost;
	
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
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	
}
