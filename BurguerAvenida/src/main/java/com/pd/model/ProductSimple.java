package com.pd.model;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Simple")
public class ProductSimple extends Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3744067681104885037L;

	public ProductSimple() {
		super();
	}
	
	public ProductSimple(String name, Double price, IVA iva) {
		super(name, price, iva);
	}
	
}
