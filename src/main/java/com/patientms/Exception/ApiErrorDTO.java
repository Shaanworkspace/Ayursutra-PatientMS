package com.patientms.Exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiErrorDTO {
	private int status;
	private String message;
	private LocalDateTime timestamp;
}
