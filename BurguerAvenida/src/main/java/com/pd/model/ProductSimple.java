package com.pd.model;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Simple")
public class ProductSimple extends ProductComposite implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3744067681104885037L;

	public ProductSimple() {
		super();
	}
	
	public ProductSimple(String name, Double price) {
		super(name, price);
	}
	
	/*public ProductoSimple(String name, Double price, FamiliaProducto familiaProducto) {
		super(name, price);
		this.familiaProducto = familiaProducto;
	}
	
	@OneToOne(cascade=CascadeType.ALL)
	private FamiliaProducto familiaProducto;

	public FamiliaProducto getFamiliaProducto() {
		return familiaProducto;
	}

	public void setFamiliaProducto(FamiliaProducto familiaProducto) {
		this.familiaProducto = familiaProducto;
	}*/
	
}
