package com.patientms.DTO.Request;

import com.patientms.ENUM.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordUpdateRequest {
	private Boolean needTherapy;
	private Status sessionStatus;
}
