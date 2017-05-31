package com.pd.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.pd.model.IVA;
import com.pd.model.ProductFamily;

public class ProductListDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3681521520345300616L;

	private String name;
	
	private Double price;
	
	private IVA iva;
	
	private Set<ProductFamily> families = new HashSet<ProductFamily>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public IVA getIva() {
		return iva;
	}

	public void setIva(IVA iva) {
		this.iva = iva;
	}

	public Set<ProductFamily> getFamilies() {
		return families;
	}

	public void setFamilies(Set<ProductFamily> families) {
		this.families = families;
	}
	
}
