package com.pd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity(name = "product")
@Table(name = "product")
public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3623051208002145067L;

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@NotNull @Column(length = 32)
	private String name;
	@NotNull @Column(length = 8, precision = 2)
	private Double cost;
	@NotNull @Enumerated(EnumType.STRING)
	private ProductType productType;

	@OneToMany(fetch = FetchType.EAGER)
	private List<Ingredient> ingredients = new ArrayList<Ingredient>();
	
	public Product() {
		super();
	}

	public Product(Integer id, String name, Double cost, ProductType productType, List<Ingredient> ingredients) {
		super();
		this.id = id;
		this.name = name;
		this.cost = cost;
		this.productType = productType;
		this.ingredients = ingredients;
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

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", cost=" + cost + ", productType=" + productType
				+ ", ingredients=" + ingredients + "]";
	}

}
