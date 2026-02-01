package com.patientms.Client.Fallback;

import com.patientms.Client.DoctorClient;
import com.patientms.DTO.Request.MedicalRecordTransfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DoctorClientFallback implements DoctorClient {
	@Override
	public boolean checkDoctorByUserId(String userId) {
		log.warn("Falling back from Patient MS as NO Doc found by User Id : {} in Doc Microservice ",userId);
		return false;
	}

	@Override
	public boolean addMedicalIdToDoctorId(MedicalRecordTransfer medicalRecordToDoctor) {
		log.warn("Fall Back as not able to add medical record");
		return false;
	}
}
