package com.patientms.DTO.Response;


import com.patientms.ENUM.Status;
import com.patientms.ENUM.TherapistDecisionStatus;
import com.patientms.ENUM.TherapyPlanStatus;
import com.patientms.ENUM.TherapyType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TherapyPlanDTO {
	private String therapyPlanId;

	private TherapyType therapyType;

	private int totalSessions;
	private int completedSessions;

	private int sessionDurationMinutes;
	private String frequency;
	private TherapistDecisionStatus therapistDecisionStatus;
	private TherapyPlanStatus status;

	private LocalDate startDate;


	private String therapistId;
	private String therapistName;

	private String therapistNotes;
}
