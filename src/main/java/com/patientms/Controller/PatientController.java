package com.patientms.Controller;

import com.patientms.DTO.Request.MedicalRecordRequestDTO;
import com.patientms.DTO.Request.RegisterRequestDTO;
import com.patientms.DTO.Response.PatientResponseDTO;
import com.patientms.Entity.Patient;
import com.patientms.Service.PatientService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final RestTemplate restTemplate;

    // ======================================
    //               GET METHODS
    // ======================================
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("PATIENT SERVICE UP");
    }
    /** Get all patients */
    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    /** Get patient by ID */
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    /** Get patient by phone */
    @GetMapping("/phone/{phone}")
    public Patient getPatientByPhone(@PathVariable String phone) {
        return patientService.getPatientByPhone(phone);
    }

    /** Search patients by name (first or last) */
    @GetMapping("/search")
    public List<Patient> searchPatientsByName(@RequestParam String name) {
        return patientService.searchPatientsByName(name);
    }

    /** Get patients by blood group */
    @GetMapping("/blood-group/{bloodGroup}")
    public List<Patient> getPatientsByBloodGroup(@PathVariable String bloodGroup) {
        return patientService.getPatientsByBloodGroup(bloodGroup);
    }




    // ======================================
    //               POST METHODS
    // ======================================

    /** Register a new patient */
    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody RegisterRequestDTO patient) {
        try {
            PatientResponseDTO savedPatient = patientService.registerPatient(patient);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }






    // ======================================
    //               PUT METHODS
    // ======================================


    /** Add a medical report to a patient */
    @PutMapping("/{patientId}/medical-records")
    public ResponseEntity<PatientResponseDTO> addMedicalRecord(
            @PathVariable Long patientId,
            @RequestBody MedicalRecordRequestDTO dto) {

        PatientResponseDTO updated = patientService.addMedicalRecord(patientId, dto);
        return ResponseEntity.ok(updated);
    }

    // ======================================
    //              DELETE METHODS
    // ======================================

    /** Delete patient by ID */
    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }
}