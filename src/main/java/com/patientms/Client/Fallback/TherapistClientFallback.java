package com.patientms.Client.Fallback;

import com.patientms.Client.TherapistClient;
import com.patientms.DTO.Response.TherapyPlanDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TherapistClientFallback implements TherapistClient {

	@Override
	public boolean checkTherapistByUserId(String id) {
		log.warn("Falling back from Patient MS as NO therapist found by Id : {} in Therapist Microservice ",id);
		return false;
	}

	@Override
	public TherapyPlanDTO getTherapyPlanByMedicalRecordId(String medicalRecordId) {
		log.warn("Falling back from Patient MS as No therapist Plan is found by medical Id : {} in Therapist Microservice ",medicalRecordId);
		return null;
	}
}
