package com.javaweb.customexceptions;

public class InvalidDataException extends RuntimeException {
	public InvalidDataException(String message) {
		super(message);
	}
}
