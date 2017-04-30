package com.pd.model.security;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "role")
public class Role implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 165143835352267665L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
	
    @NotNull
    @Enumerated(EnumType.STRING)
    private RoleName name;
    
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;
    
	public Role() {
		super();
	}
	
	public Role(RoleName name) {
		super();
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public RoleName getName() {
		return name;
	}

	public void setName(RoleName name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}
    
}
