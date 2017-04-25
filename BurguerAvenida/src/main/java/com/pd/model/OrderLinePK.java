package com.pd.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderLinePK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4662118930818906698L;
	
	@Column(name = "order_id")
	private Integer orderId;
	
	@Column(name = "product_id")
	private Integer productId;
	
	public OrderLinePK() {
		super();
	}

	public OrderLinePK(Integer orderId, Integer productId) {
		super();
		this.orderId = orderId;
		this.productId = productId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public Integer getProductId() {
		return productId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
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
		OrderLinePK other = (OrderLinePK) obj;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		return true;
	}
	
}
