package com.patientms.Controller;

import com.patientms.DTO.Request.MedicalRecordRequestDTO;
import com.patientms.DTO.Request.TherapyUpdateRequest;
import com.patientms.DTO.Response.MedicalRecordResponseDTO;
import com.patientms.Entity.MedicalRecord;
import com.patientms.Messaging.MedicalRecordMessageProducer;
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
    private final MedicalRecordMessageProducer medicalRecordMessageProducer;
    // ======================================
    //              GET METHODS
    // ======================================
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("PATIENT SERVICE UP");
    }
    /** Get all records */
    @GetMapping
    public ResponseEntity<List<MedicalRecordResponseDTO>> getAllRecords() {
        return ResponseEntity.ok(medicalRecordService.getAllMedicalRecords());
    }

    /** Get record by ID */
    @GetMapping("/{recordId}")
    public ResponseEntity<MedicalRecordResponseDTO> getRecordById(@PathVariable Long recordId) {
        try {
            MedicalRecordResponseDTO record = medicalRecordService.getMedicalRecordById(recordId);
            return ResponseEntity.ok(record);
        } catch (IllegalArgumentException e) {
            // If the record isn’t found, return 404 with an error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
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


    /** Get all records for a patient */
    @GetMapping("/patient")
    public ResponseEntity<List<MedicalRecordResponseDTO>> getRecordsByPatient(@RequestParam  String patientId) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecordsByPatient(patientId));
    }

    /** Get all records for a doctor */
    @GetMapping("/doctor")
    public ResponseEntity<List<MedicalRecordResponseDTO>> getRecordsByDoctor(@RequestParam String doctorId) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecordsByDoctor(doctorId));
    }

    // ======================================
    //              POST METHODS
    // ======================================

    /**
     * Create a medical record (patient + doctor passed in)
     */
    @PostMapping("/book")
    public ResponseEntity<Boolean> createMedicalRecord(@RequestBody MedicalRecordRequestDTO dto) {
        Boolean saved = medicalRecordService.createFromPatientRequest(dto);
        log.info("Saving Medical record with data : {} with saving status : {}",dto,saved);

        if(saved) return ResponseEntity.status(HttpStatus.CREATED).body(true);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
    }

    // ======================================
    //              PUT METHODS
    // ======================================

    /** Update therapies (assign or modify therapy list for a record) */
    @PutMapping("/{recordId}/therapies")
    public ResponseEntity<?> updateTherapies(
            @PathVariable Long recordId,
            @RequestBody TherapyUpdateRequest req) {
        try {
            MedicalRecord updated = medicalRecordService.updateTherapies(
                    recordId,
                    req.isNeedTherapy(),
                    req.getTherapyIds()
            );
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No!!! " + e.getMessage());
        }
    }

    /** Assign therapist to a medical record */
    @PutMapping("/{recordId}/assign-therapist")
    public ResponseEntity<?> assignTherapistToRecord(@PathVariable Long recordId,
                                                     @RequestParam String therapistId) {
        try {
            MedicalRecord updated = medicalRecordService.assignTherapist(recordId, therapistId);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ " + e.getMessage());
        }
    }

    /** Update a medical record (doctor adds diagnosis, treatment, etc.) */
    @PutMapping("/{recordId}")
    public ResponseEntity<?> updateMedicalRecord(@PathVariable Long recordId, @RequestBody MedicalRecord updatedData) {
        try {
            MedicalRecord updated = medicalRecordService.updateMedicalRecord(recordId, updatedData);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ " + e.getMessage());
        }
    }

    // ======================================
    //              DELETE METHODS
    // ======================================

    /** Delete a medical record */
    @DeleteMapping("/{recordId}")
    public ResponseEntity<String> deleteRecord(@PathVariable Long recordId) {
        try {
            medicalRecordService.deleteMedicalRecord(recordId);
            return ResponseEntity.ok("Medical record deleted with ID: " + recordId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ " + e.getMessage());
        }
    }
}