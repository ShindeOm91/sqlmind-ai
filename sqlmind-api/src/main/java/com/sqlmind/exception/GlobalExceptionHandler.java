package com.sqlmind.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sqlmind.common.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExists(
            UserAlreadyExistsException exception) {

        ApiResponse<Void> response = new ApiResponse<>(
                false,
                exception.getMessage(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(
	        MethodArgumentNotValidException exception) {

	    Map<String, String> errors = new HashMap<>();

	    exception.getBindingResult()
	            .getFieldErrors()
	            .forEach(error ->
	                    errors.put(error.getField(), error.getDefaultMessage())
	            );

	    ApiResponse<Map<String, String>> response =
	            new ApiResponse<>(
	                    false,
	                    "Validation failed",
	                    errors
	            );

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(response);
	}
	
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ApiResponse<Void>> handleInvalidCredentials(
	        InvalidCredentialsException exception) {

	    ApiResponse<Void> response = new ApiResponse<>(
	            false,
	            exception.getMessage(),
	            null
	    );

	    return ResponseEntity
	            .status(HttpStatus.UNAUTHORIZED)
	            .body(response);
	}
	
}
