package com.patientms.Service;

import com.patientms.DTO.Request.MedicalRecordRequestDTO;
import com.patientms.DTO.Response.PatientResponseDTO;
import com.patientms.Entity.Patient;
import com.patientms.Repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final RestTemplate restTemplate;


    @Value("${medicalrecord.service.url}")
    private String medicalRecordServiceUrl;



    public Patient registerPatient(Patient patient) {
        // Check if email already exists
        if (patientRepository.findByEmail(patient.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Patient with this email already exists");
        }

        // Let timestamps be handled by entity @PrePersist
        return patientRepository.save(patient);
    }


    public PatientResponseDTO patientToDto(Patient patient) {
                return new PatientResponseDTO(
                patient.getId(), patient.getFirstName() +" "+ patient.getLastName()
                ,patient.getGender(),patient.getEmail(), patient.getPhoneNumber(), patient.getMedicalRecordsIds()
        );
    }

    // Get patient by ID
    public PatientResponseDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with id: " + id));

        return patientToDto(patient);
    }

    //  Get all patients with mapped DTOs
    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::patientToDto)
                .collect(Collectors.toList());
    }


    //Delete patient
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    // Get patient by email (for login / lookup)
    public Patient getPatientByEmail(String email) {
        return patientRepository.findByEmail(email).orElse(null);
    }

    // Get patient by phone number
    public Patient getPatientByPhone(String phoneNumber) {
        return patientRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }

    //  Search patients by first or last name (case-insensitive)
    public List<Patient> searchPatientsByName(String name) {
        return patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }

    // Filter patients by blood group
    public List<Patient> getPatientsByBloodGroup(String bloodGroup) {
        return patientRepository.findByBloodGroup(bloodGroup);
    }


    /** Book an appointment and create medical record */
    public PatientResponseDTO addMedicalRecord(Long patientId, MedicalRecordRequestDTO medicalRecordRequestDTO){
        Patient patient = patientRepository.findById(patientId).orElseThrow(()-> new IllegalArgumentException("Patient not found with ID: " + patientId));
        medicalRecordRequestDTO.setPatientId(patientId);

        Long newRecordId = restTemplate.postForObject(medicalRecordServiceUrl, medicalRecordRequestDTO, Long.class);

        if (patient.getMedicalRecordsIds() == null)
            patient.setMedicalRecordsIds(new ArrayList<>());

        patient.getMedicalRecordsIds().add(newRecordId);
        patientRepository.save(patient);

        return patientToDto(patient);
    }

}
