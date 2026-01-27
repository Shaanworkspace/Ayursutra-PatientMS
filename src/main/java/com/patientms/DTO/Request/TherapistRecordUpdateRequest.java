package com.patientms.DTO.Request;

import com.patientms.ENUM.TherapyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TherapistRecordUpdateRequest {

	private Boolean needTherapy;
	private List<TherapyType> therapies;

	private String therapistNotes;   // future use
	private String sessionStatus;    // COMPLETED / FOLLOW_UP
}
