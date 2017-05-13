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
	
	public ProductSimple(String name, String price, IVA iva, Boolean cantBeSoldAlone) {
		super(name, price, iva, cantBeSoldAlone);
	}

	@Override
	public String toString() {
		return getName();
	}
	
}
