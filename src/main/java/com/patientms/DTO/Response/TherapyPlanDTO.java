package com.patientms.DTO.Response;


import com.patientms.ENUM.TherapistDecisionStatus;
import com.patientms.ENUM.TherapyPlanStatus;
import com.patientms.ENUM.TherapyType;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TherapyPlanDTO {
	private String therapyPlanId;

	private List<TherapyType> therapies;


	private int totalTherapySessions;
	private int completedTherapySessions;

	private int sessionDurationMinutes;
	private String frequency;
	private TherapistDecisionStatus therapistDecisionStatus;
	private TherapyPlanStatus status;

	private LocalDate startDate;
	private String therapistId;
	private String therapistName;

	private String therapistNotes;
}
