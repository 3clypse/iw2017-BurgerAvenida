package com.pd.model;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
@DiscriminatorValue("Composite")
public class ProductComposite extends Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7221091527474882362L;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name="product_composite",
	    joinColumns={ @JoinColumn(name="product_composite_id", referencedColumnName="id") },
	    inverseJoinColumns={ @JoinColumn(name="product_id", referencedColumnName="id") }
	)
	private Set<Product> products = new HashSet<Product>();
	
	public ProductComposite() {
		super();
	}
	
	public ProductComposite(String name, Double price, IVA iva, Boolean canBeSoldAlone) {
		super(name, price, iva, canBeSoldAlone);
	}
	
	public ProductComposite(String name, Double price, IVA iva, Set<Product> products, Boolean canBeSoldAlone) {
		super(name, price, iva, canBeSoldAlone);
		this.products = products;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
