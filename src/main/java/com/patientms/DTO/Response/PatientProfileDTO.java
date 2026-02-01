package com.patientms.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientProfileDTO {
	private String email;
	private String userId;
	private String gender;
	private String phoneNumber;
	@Builder.Default
	private String role="PATIENT";
}
