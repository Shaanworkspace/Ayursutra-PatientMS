package com.patientms.ENUM;

public enum TherapyPlanStatus {
	NOT_ASSIGNED,
	ASSIGNED,   // Plan created, sessions not started
	ACTIVE,     // Sessions ongoing
	COMPLETED   // All sessions completed
}
