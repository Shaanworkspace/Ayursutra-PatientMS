package com.patientms.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
		name ="therapist-service",
		url = "${services.therapist.url}"
)
public interface TherapistClient {

	@GetMapping("/api/therapists/exist/{id}")
	boolean checkTherapistByUserId(@PathVariable String id);
}
