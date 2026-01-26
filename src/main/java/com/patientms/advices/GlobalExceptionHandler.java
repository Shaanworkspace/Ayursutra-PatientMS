package com.patientms.advices;



import com.patientms.Exception.ApiErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiErrorDTO> handleRuntimeException(RuntimeException e){
		ApiErrorDTO apiError = ApiErrorDTO.builder()
				.message(e.getMessage())
				.status(HttpStatus.BAD_REQUEST.value())
				.timestamp(LocalDateTime.now())
				.build();
		return new ResponseEntity<>(apiError,HttpStatus.BAD_REQUEST);
	}
}
