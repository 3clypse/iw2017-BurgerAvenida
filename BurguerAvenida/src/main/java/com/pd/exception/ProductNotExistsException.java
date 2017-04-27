package com.pd.exception;

public class ProductNotExistsException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4567069796468911931L;
	
	private static final String MSG = "Cant find product";
	
	public ProductNotExistsException() {
		super(MSG);
	}

}
