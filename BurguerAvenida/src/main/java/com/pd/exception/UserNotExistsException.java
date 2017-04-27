package com.pd.exception;

public class UserNotExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7318162185149509278L;
	
	private static final String MSG = "El usuario no existe";

	public UserNotExistsException() {
		super(MSG);
	}
	
}
