package com.mapsdirections.lib.exceptions;

public class DirectionsException extends Exception {

	private static final long serialVersionUID = 1L;

	private String message;
	private int directionError;

	public DirectionsException(int directionError) {
		this.directionError = directionError;
	}

	public DirectionsException(int directionError, String message) {
		this(directionError);
		this.message = message;
	}

	public int getDirectionError() {
		return directionError;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
