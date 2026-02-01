package com.patientms.Service;

import com.patientms.DTO.Request.MedicalRecordRequestDTO;
import com.patientms.DTO.Request.RegisterRequestDTO;
import com.patientms.DTO.Response.MedicalRecordResponseDTO;
import com.patientms.DTO.Response.PatientProfileDTO;
import com.patientms.DTO.Response.PatientResponseDTO;
import com.patientms.Entity.MedicalRecord;
import com.patientms.Entity.Patient;
import com.patientms.Repository.MedicalRecordRepository;
import com.patientms.Repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordService medicalRecordService;

    public PatientResponseDTO registerPatient(RegisterRequestDTO registerRequest) {
        if(patientRepository.existsByEmail((registerRequest.getEmail()))){
            Patient existingPatient = patientRepository.findByEmail(registerRequest.getEmail());
            return patientToDto(existingPatient);
        }
        Patient patient = Patient.builder()
                .email(registerRequest.getEmail())
                .userId(registerRequest.getUserId())
                .name(registerRequest.getName())
                .password(registerRequest.getPassword())
                .build();
        patientRepository.save(patient);
        return patientToDto(patient);
    }


    public PatientResponseDTO patientToDto(Patient patient) {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findMedicalRecordsByPatientId(patient.getUserId());
        List<MedicalRecordResponseDTO> medicalRecordResponseDTOList = medicalRecords.stream()
                .map(medicalRecordService::medicalRecordConvertToMedicalRecordResponseDTO)
                .toList();

        return PatientResponseDTO.builder()
                .email(patient.getEmail())
               .gender(patient.getGender())
               .medicalRecords(medicalRecordResponseDTOList)
               .phoneNumber(patient.getPhoneNumber())
               .userId(patient.getUserId())
               .build();
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
        return null;
    }

	public PatientProfileDTO patientToProfileDto(Patient patient) {
        return PatientProfileDTO.builder()
                .email(patient.getEmail())
                .gender(patient.getGender())
                .phoneNumber(patient.getPhoneNumber())
                .userId(patient.getUserId())
                .build();
    }
}
