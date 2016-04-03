package com.tubager.exception;

public class InvalidEmail extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7241216828108338831L;

	public InvalidEmail(String msg){
		super(msg);
	}

}
