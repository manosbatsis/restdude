package com.restdude.util.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * @see com.restdude.domain.base.controller.GlobalExceptionHandler
 */
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;
	
	private static List<String> stringToList(String s){
		ArrayList<String> list = new ArrayList<String>(1);
		list.add(s);
		return list;
	}
	
	private List<String> errors;
	
	@JsonIgnore
	boolean complete;

    public BadRequestException(String message) {
        this(message, (List<String>) null, false);
	}

    public BadRequestException(String message, List<String> errors) {
        this(message, errors, false);
	}


    public BadRequestException(String message, String error, boolean complete) {
        this(message, stringToList(error), false);
	}

    public BadRequestException(String message, List<String> errors, boolean complete) {
        super(message);
		this.complete = complete;
		this.errors = errors;
	}

	public List<String> getErrors() {
		return errors;
	}

	@JsonIgnore
	@Override
	public synchronized Throwable getCause() {
		// TODO Auto-generated method stub
		return super.getCause();
	}

	@JsonIgnore
	@Override
	public String getLocalizedMessage() {
		return super.getLocalizedMessage();
	}

	@Override
	public StackTraceElement[] getStackTrace() {
		return this.complete ? super.getStackTrace() : null ;
	}
	
	

}
