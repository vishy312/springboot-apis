package com.raster;

import java.util.List;

public class SaveBusinessResponse {
	private final boolean success;
	private final String message;
	private final List<String> errors;
	
	public SaveBusinessResponse(boolean success, String message, List<String> errors) {
		this.success = success;
		this.message = message;
		this.errors = errors;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public List<String> getErrors() {
		return errors;
	}

}
