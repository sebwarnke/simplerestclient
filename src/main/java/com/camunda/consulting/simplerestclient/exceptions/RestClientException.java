package com.camunda.consulting.simplerestclient.exceptions;

public class RestClientException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8608307099189791709L;

	public RestClientException(String message) {
		super(message);
	}

	public RestClientException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
