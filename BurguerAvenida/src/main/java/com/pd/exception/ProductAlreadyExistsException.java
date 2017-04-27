package com.pd.exception;

public class ProductAlreadyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3894832023281330732L;
	
	private static final String MSG = "Cant find product";
	
	public ProductAlreadyExistsException() {
		super(MSG);
	}

}
