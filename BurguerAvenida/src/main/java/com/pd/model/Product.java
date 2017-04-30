package com.pd.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="ProductType")
public class Product {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO) 
	private Integer id;
	
	@Column(length = 32, unique = true)
	private String name;
	
	private Double price;
	
	private IVA iva;
	
	public Product() {
		super();
	}
	
	public Product(String name, Double price, IVA iva) {
		super();
		this.name = name;
		this.price = price;
		this.iva = iva;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public IVA getIva() {
		return iva;
	}

	public void setIva(IVA iva) {
		this.iva = iva;
	}
	
}
