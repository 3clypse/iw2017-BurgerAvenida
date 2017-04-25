package com.pd.model;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("Composite")
public class ProductComposite extends Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7221091527474882362L;
	
	@OneToMany
	@JoinTable(
		name="product_composite",
	    joinColumns={ @JoinColumn(name="product_composite_id", referencedColumnName="id") },
	    inverseJoinColumns={ @JoinColumn(name="product_id", referencedColumnName="id") }
	)
	private Set<Product> products = new HashSet<Product>();
	
	/*@OneToMany(cascade=CascadeType.ALL)
	private Set<ProductFamily> families = new HashSet<ProductFamily>();*/
	
	public ProductComposite() {
		super();
	}

	public ProductComposite(String name, Double price) {
		super(name, price);
	}
	
	public ProductComposite(String name, Double price, Set<Product> products) {
		super(name, price);
		this.products = products;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	/*public Set<ProductFamily> getFamilies() {
		return families;
	}

	public void setFamilies(Set<ProductFamily> families) {
		this.families = families;
	}*/
	
}
