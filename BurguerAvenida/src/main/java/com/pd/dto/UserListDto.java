package com.pd.dto;

import java.io.Serializable;

public class UserListDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3313360027055957860L;
	
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String Email;
	
	public UserListDto() {
		super();
	}

	public UserListDto(String username, String password, String firstname, String lastname, String email) {
		super();
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		Email = email;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
