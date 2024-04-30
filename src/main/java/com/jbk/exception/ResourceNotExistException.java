package com.jbk.exception;

public class ResourceNotExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotExistException(String msg) {
		super(msg);
	}
}
