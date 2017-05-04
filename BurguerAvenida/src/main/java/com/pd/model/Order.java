package com.pd.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "orders")
public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7221091527474882362L;
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO) 
	private Integer id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date closedAt;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	@Enumerated(EnumType.STRING)
	private OrderType type;
	
	@ManyToOne
	private Zone zone;

	@ManyToOne
	private Client client;
	
	public Order() {
		super();
		this.status = OrderStatus.OPENED;
		this.createdAt = new Date();
	}
	
	@OneToMany(mappedBy = "orderObject")
	private Set<OrderLine> lines = new HashSet<OrderLine>();

	public Date getClosedAt() {
		return closedAt;
	}

	public void setClosedAt(Date closedAt) {
		this.closedAt = closedAt;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Set<OrderLine> getLines() {
		return lines;
	}

	public void setLines(Set<OrderLine> lines) {
		this.lines = lines;
	}

	public Integer getId() {
		return id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}
	
}
