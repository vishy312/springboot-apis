package com.raster;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(BusinessNotFoundException.class)
	public ResponseEntity<ErrorResponseException> handleBusinessNotFound(BusinessNotFoundException ex){
		ErrorResponseException error = new ErrorResponseException(HttpStatus.NOT_FOUND);
		error.setDetail(ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);	
	}
	
	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<ErrorResponseException> handleInvalidRequest(InvalidRequestException ex){
		ErrorResponseException error = new ErrorResponseException(HttpStatus.BAD_REQUEST);
		error.setDetail("Invalid Request");
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(PostBusinessException.class)
	public ResponseEntity<ErrorResponseException> handlePostErrors(PostBusinessException ex){
		ErrorResponseException error = new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR);
		error.setDetail("Post could not be completed");
		
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(PutBusinessException.class)
	public ResponseEntity<ErrorResponseException> handlePutErrors(PutBusinessException ex){
		ErrorResponseException error = new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR);
		error.setDetail("Put could not be completed");
		
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
