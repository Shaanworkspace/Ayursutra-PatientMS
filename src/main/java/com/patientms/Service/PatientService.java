package com.patientms.Service;

import com.patientms.DTO.Request.MedicalRecordRequestDTO;
import com.patientms.DTO.Request.RegisterRequestDTO;
import com.patientms.DTO.Response.PatientResponseDTO;
import com.patientms.Entity.Patient;
import com.patientms.Repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientResponseDTO registerPatient(RegisterRequestDTO registerRequest) {
        if(patientRepository.existsByUserId((registerRequest.getUserId()))){
            Patient existingPatient = patientRepository.findByUserId(registerRequest.getUserId());
            return patientToDto(existingPatient);
        }
        Patient patient = Patient.builder()
                .userId(registerRequest.getUserId())
                .password(registerRequest.getPassword())
                .build();
        patientRepository.save(patient);
        return patientToDto(patient);
    }


    public PatientResponseDTO patientToDto(Patient patient) {
                return new PatientResponseDTO(
                patient.getUserId()
                ,patient.getGender(), patient.getPhoneNumber(), null
        );
    }

    // Get patient by ID
    public Patient getPatientByUserId(String id) {
	    return patientRepository.findByUserId(id);
    }

    //  Get all patients with mapped DTOs
    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::patientToDto)
                .collect(Collectors.toList());
    }


    //Delete patient
    public void deletePatient(String id) {
        patientRepository.deleteById(id);
    }

    // Get patient by phone number
    public Patient getPatientByPhone(String phoneNumber) {
        return patientRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }

    // Filter patients by blood group
    public List<Patient> getPatientsByBloodGroup(String bloodGroup) {
        return patientRepository.findByBloodGroup(bloodGroup);
    }


    /** Book an appointment and create medical record */
    public PatientResponseDTO addMedicalRecord(Long patientId, MedicalRecordRequestDTO medicalRecordRequestDTO){
//        Patient patient = patientRepository.findById(patientId).orElseThrow(()-> new IllegalArgumentException("Patient not found with ID: " + patientId));
//        medicalRecordRequestDTO.setPatientId(patientId);
//
//        Long newRecordId = restTemplate.postForObject(medicalRecordServiceUrl, medicalRecordRequestDTO, Long.class);
//
//        if (patient.getMedicalRecordsIds() == null)
//            patient.setMedicalRecordsIds(new ArrayList<>());
//
//        patient.getMedicalRecordsIds().add(newRecordId);
//        patientRepository.save(patient);
//
//        return patientToDto(patient);
        return null;
    }

}
