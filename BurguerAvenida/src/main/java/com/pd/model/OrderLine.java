package com.pd.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity(name = "orderlines")
public class OrderLine implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2394408194092400146L;

	@EmbeddedId
	private OrderLinePK id;
	
	private Integer amount;

	@MapsId("orderId")
    @JoinColumn(name="order_id")
	@ManyToOne
    private Order orderObject;
	
	@MapsId("productId")
    @JoinColumn(name="product_id")
	@ManyToOne
    private Product product;
	
	private Double total;
	
	private Boolean isInKitchen;
	
	public OrderLine() {
		super();
	}

	public OrderLine(Order orderObject, Product product, Integer amount) {
		super();
		this.orderObject = orderObject;
		this.product = product;
		this.id = new OrderLinePK(orderObject.getId(), product.getId());
		this.amount = amount;
		this.isInKitchen = false;
		calculateTotal();
	}
	
	public OrderLine(Order orderObject, Product product) {
		super();
		this.orderObject = orderObject;
		this.product = product;
		this.id = new OrderLinePK(orderObject.getId(), product.getId());
		this.isInKitchen = false;
		calculateTotal();
	}
	
	public Double getTotal() {
		return total;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
		calculateTotal();
	}

	public Order getOrder() {
		return orderObject;
	}

	public Product getProduct() {
		return product;
	}

	public OrderLinePK getId() {
		return id;
	}
	
	public Order getOrderObject() {
		return orderObject;
	}

	public void setOrderObject(Order orderObject) {
		this.orderObject = orderObject;
	}

	public void calculateTotal() {
		this.total = (product.getPrice() * (1 + product.getIva().getIVA()) ) * amount;
	}
	
	public Boolean getIsInKitchen() {
		return isInKitchen;
	}

	public void setIsInKitchen(Boolean isInKitchen) {
		this.isInKitchen = isInKitchen;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderLine other = (OrderLine) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
