package com.filmrental.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler({ FilmNotFoundException.class })
	public ResponseEntity<Object> filmNotFoundException(FilmNotFoundException noFilm) {
		Map<String, Object> error = new HashMap<>();
		error.put("errorCode", 301);
		error.put("timestamp", LocalDateTime.now());
		error.put("errorMessage", noFilm.getMessage());
		return ResponseEntity.badRequest().body(error);
	}

	@ExceptionHandler({ ActorNotFoundException.class })
	public ResponseEntity<Object> handleStudentNotFoundExection(ActorNotFoundException nda) {
		LocalDate dateTime = LocalDate.now();
		ErrorDetails details = new ErrorDetails(dateTime, nda.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(details);
	}
}
