package com.validator.exception;

public class ExcelValidatorException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public ExcelValidatorException(String msg, Throwable causa) {
		super(msg, causa);
	}
	
}
