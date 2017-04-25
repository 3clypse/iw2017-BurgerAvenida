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
}
