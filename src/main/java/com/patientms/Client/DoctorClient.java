package com.patientms.Client;

import com.patientms.DTO.Request.MedicalRecordTransfer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
		name ="doctor-service",
		url = "${services.doctor.url}"
)
public interface DoctorClient {
	@GetMapping("/api/doctors/check/{userId}")
	boolean checkDoctorByUserId(@PathVariable String userId);

	@PutMapping("/api/doctors/add-medicalId")
	boolean addMedicalIdToDoctorId(@RequestParam MedicalRecordTransfer medicalRecordToDoctor);
}
