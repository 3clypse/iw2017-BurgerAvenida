package com.pd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "productOrder")
@Table(name = "productOrder")
public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8453470360678797120L;
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	@Enumerated(EnumType.STRING)
	private OrderState orderState;
	@Enumerated(EnumType.STRING)
	private OrderType orderType;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Client client;
	
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Product> products = new ArrayList<Product>();

	public Order() {
		super();
	}

	public Order(Integer id, Date createdDate, OrderState orderState, OrderType orderType, Client client,
			List<Product> products) {
		super();
		this.id = id;
		this.createdDate = createdDate;
		this.orderState = orderState;
		this.orderType = orderType;
		this.client = client;
		this.products = products;
	}

	public Integer getId() {
		return id;
	}

	protected void setId(Integer id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public OrderState getOrderState() {
		return orderState;
	}

	public void setOrderState(OrderState orderState) {
		this.orderState = orderState;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
}
