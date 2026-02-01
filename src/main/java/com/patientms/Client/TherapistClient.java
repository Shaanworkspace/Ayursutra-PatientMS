package com.patientms.Client;

import com.patientms.DTO.Response.TherapyPlanDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
		name ="therapist-service",
		url = "${services.therapist.url}"
)
public interface TherapistClient {

	@GetMapping("/api/therapists/exist/{id}")
	boolean checkTherapistByUserId(@PathVariable String id);

	@GetMapping("/api/therapists/therapy-plans/medical-record/{medicalRecordId}")
	TherapyPlanDTO getTherapyPlanByMedicalRecordId(
			@PathVariable String medicalRecordId
	);
}
