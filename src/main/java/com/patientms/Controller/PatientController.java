package com.patientms.Controller;

import com.patientms.DTO.Request.MedicalRecordRequestDTO;
import com.patientms.DTO.Request.RegisterRequestDTO;
import com.patientms.DTO.Response.MedicalRecordResponseDTO;
import com.patientms.DTO.Response.PatientProfileDTO;
import com.patientms.DTO.Response.PatientResponseDTO;
import com.patientms.Entity.MedicalRecord;
import com.patientms.Entity.Patient;
import com.patientms.Repository.MedicalRecordRepository;
import com.patientms.Repository.PatientRepository;
import com.patientms.Service.MedicalRecordService;
import com.patientms.Service.PatientService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    private final PatientService patientService;
    private final RestTemplate restTemplate;
    private final PatientRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordService medicalRecordService;

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
    @GetMapping("/check/{id}")
    public boolean getPatientById(@PathVariable String id) {
        return patientRepository.existsByUserId(id);
    }

    @GetMapping("/all/medical-records/{patientId}")
    public ResponseEntity<List<MedicalRecordResponseDTO>> medicalRecordsByPatientId(@PathVariable String patientId) {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findMedicalRecordsByPatientId(patientId);
        List<MedicalRecordResponseDTO> medicalRecordResponseDTOList = medicalRecords.stream()
                .map(medicalRecordService::medicalRecordConvertToMedicalRecordResponseDTO)
                .toList();
        return ResponseEntity.ok(medicalRecordResponseDTOList);
    }

    @GetMapping("/profile/me")
    public ResponseEntity<PatientProfileDTO> getMyProfile(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        boolean exist = patientRepository.existsByEmail(email);
        if (!exist){
            throw new RuntimeException(
                    "Patient not Exist for email: " + email
            );
        }
        Patient patient = patientRepository.findByEmail(email);
        if (patient == null){
            log.info("Patient not found for email: {}" , email);
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(patientService.patientToProfileDto(patient));
    }


    /** Get patient by phone */
    @GetMapping("/phone/{phone}")
    public Patient getPatientByPhone(@PathVariable String phone) {
        return patientService.getPatientByPhone(phone);
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
    public void deletePatient(@PathVariable String id) {
        patientService.deletePatient(id);
    }
}