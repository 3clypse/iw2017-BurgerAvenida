package com.pd.exception;


public class UserAlreadyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2419452203660794647L;
	
	private static final String MSG = "El usuario ya existe";

	public UserAlreadyExistsException() {
		super(MSG);
	}

}