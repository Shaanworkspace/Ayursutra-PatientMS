package com.patientms.Controller;

import com.patientms.DTO.Request.MedicalRecordRequestDTO;
import com.patientms.DTO.Request.MedicalRecordUpdateRequest;
import com.patientms.DTO.Response.MedicalRecordResponseDTO;
import com.patientms.Entity.MedicalRecord;
import com.patientms.Repository.MedicalRecordRepository;
import com.patientms.Service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/patients/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {


	private final MedicalRecordService medicalRecordService;
	private final MedicalRecordRepository medicalRecordRepository;

	// ======================================
	//              GET METHODS
	// ======================================
	@GetMapping("/health")
	public ResponseEntity<String> health() {
		return ResponseEntity.ok("Medical SERVICE UP");
	}

	/**
	 * Get all records
	 */
	@GetMapping
	public ResponseEntity<List<MedicalRecordResponseDTO>> getAllRecords() {
		return ResponseEntity.ok(medicalRecordService.getAllMedicalRecords());
	}

	/**
	 * Get record by ID
	 */
	@GetMapping("/{recordId}")
	public ResponseEntity<MedicalRecordResponseDTO> getRecordById(@PathVariable String recordId) {
		try {
			log.info("Get record by id: {}", recordId);
			MedicalRecordResponseDTO record = medicalRecordService.getMedicalRecordByMedicalId(recordId);
			return ResponseEntity.ok(record);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping("/check/{recordId}")
	public boolean checkRecordById(@PathVariable String recordId) {
		log.info("Checking record by id: {}", recordId);
		return medicalRecordRepository.existsMedicalRecordByMedicalRecordId(recordId);
	}

	@GetMapping("/doc/{docId}")
	public ResponseEntity<List<MedicalRecordResponseDTO>> getRecordsByDocId(@PathVariable String docId) {
		try {
			List<MedicalRecordResponseDTO> record = medicalRecordService.findAllMedicalRecordsByDoctorId(docId);
			return ResponseEntity.ok(record);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping("/batch")
	public List<MedicalRecordResponseDTO> getMedicalRecordsByIds(
			@RequestParam List<String> ids
	) {
		return medicalRecordService.getMedicalRecordsByIds(ids);
	}


	/**
	 * Get all records for a patient
	 */
	@GetMapping("/patient")
	public ResponseEntity<List<MedicalRecordResponseDTO>> getRecordsByPatient(@RequestParam String patientId) {
		return ResponseEntity.ok(medicalRecordService.getMedicalRecordsByPatientId(patientId));
	}

	/**
	 * Get all records for a doctor
	 */
	@GetMapping("/doctor")
	public ResponseEntity<List<MedicalRecordResponseDTO>> getRecordsByDoctor(@RequestParam String doctorId) {
		return ResponseEntity.ok(medicalRecordService.getMedicalRecordsByDoctorId(doctorId));
	}

	// ======================================
	//              POST METHODS
	// ======================================

	/**
	 * Create a medical record (patient + doctor passed in)
	 */
	@PostMapping("/book")
	public ResponseEntity<String> createMedicalRecord(@RequestBody MedicalRecordRequestDTO dto) {
		boolean saved = medicalRecordService.addMedicalRecord(dto);
		log.info("Saving Medical record with data : {} with saving status : {}", dto, saved);
		return ResponseEntity.status(HttpStatus.CREATED).body("Saved");
	}

	// ======================================
	//              PUT METHODS
	// ======================================

	/**
	 * Update therapies (assign or modify therapy list for a record)
	 */
	@PutMapping("/{recordId}/therapist")
	public ResponseEntity<Boolean> updateTherapies(
			@PathVariable String recordId,
			@RequestBody MedicalRecordUpdateRequest req) {

		boolean updated = medicalRecordService.updateMedicalRecord(recordId,
				req
		);
		if(updated) return ResponseEntity.ok(true);
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/{recordId}/therapist-update")
	public ResponseEntity<?> therapistUpdateRecord(
			@PathVariable String recordId,
			@RequestBody MedicalRecordUpdateRequest req
	) {
		log.info("Updating medical record by therapist with record id:{}, and re:{}", recordId, req);
		MedicalRecord updated =
				medicalRecordService.updateByTherapist(recordId, req);
		return ResponseEntity.ok(medicalRecordService.medicalRecordConvertToMedicalRecordResponseDTO(updated));
	}


	@PutMapping("/edit/{recordId}")
	public boolean editMedicalRecord(
			@PathVariable String recordId,
			@RequestBody MedicalRecordRequestDTO dto
	) {
		try {
			medicalRecordService.editMedicalRecord(recordId, dto);
			return true;
		} catch (IllegalArgumentException e) {
			log.info("Not Able to update");
			return false;
		}
	}


	// ======================================
	//              DELETE METHODS
	// ======================================

	/**
	 * Delete a medical record
	 */
	@DeleteMapping("/{recordId}")
	public ResponseEntity<String> deleteRecord(@PathVariable String recordId) {
		try {
			medicalRecordService.deleteMedicalRecord(recordId);
			return ResponseEntity.ok("Medical record deleted with ID: " + recordId);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("!!!!  " + e.getMessage());
		}
	}
}