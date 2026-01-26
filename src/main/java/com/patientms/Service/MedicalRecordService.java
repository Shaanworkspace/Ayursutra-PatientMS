package com.patientms.Service;

import com.patientms.DTO.Request.MedicalRecordRequestDTO;import com.patientms.DTO.Response.MedicalRecordResponseDTO;
import com.patientms.Entity.MedicalRecord;
import com.patientms.Entity.Patient;
import com.patientms.Repository.MedicalRecordRepository;
import com.patientms.Repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;



    // Update therapy requirements
    @Transactional
    public MedicalRecord updateTherapies(Long recordId, boolean needTherapy, List<Long> therapyIds) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Medical Record not found with id " + recordId));

        record.setNeedTherapy(needTherapy);
        return medicalRecordRepository.save(record);
    }

    // Get all medical records as DTO list
    public List<MedicalRecordResponseDTO> getAllMedicalRecords() {
        return medicalRecordRepository.findAll()
                .stream()
                .map(this::medicalRecordConvertToMedicalRecordResponseDTO)
                .collect(Collectors.toList());
    }

    // Get record by ID (converted to DTO)
    public MedicalRecordResponseDTO getMedicalRecordById(Long recordId) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Medical Record not found with ID: " + recordId));
        return medicalRecordConvertToMedicalRecordResponseDTO(record);
    }

    // Get records by patient
    public List<MedicalRecordResponseDTO> getMedicalRecordsByPatient(String patientId) {
        return medicalRecordRepository.findByPatientUserId(patientId)
                .stream()
                .map(this::medicalRecordConvertToMedicalRecordResponseDTO)
                .collect(Collectors.toList());
    }

    // Get records by doctor
    public List<MedicalRecordResponseDTO> getMedicalRecordsByDoctor(String doctorId) {
        return medicalRecordRepository.findByDoctorId(doctorId)
                .stream()
                .map(this::medicalRecordConvertToMedicalRecordResponseDTO)
                .collect(Collectors.toList());
    }

    public MedicalRecordResponseDTO medicalRecordConvertToMedicalRecordResponseDTO(
            MedicalRecord record
    ) {

        return MedicalRecordResponseDTO.builder()
                .id(record.getId())
                .patientId(record.getPatient().getUserId())
                .doctorId(record.getDoctorId())
                .therapistId(record.getTherapistId())
                .visitDate(record.getVisitDate())
                .createdDate(record.getCreatedDate())
                .symptoms(record.getSymptoms())
                .prescribedTreatment(record.getPrescribedTreatment())
                .medications(record.getMedications())
                .followUpRequired(record.getFollowUpRequired())
                .needTherapy(record.isNeedTherapy())
                .approvedByTherapist(record.isApprovedByTherapist())
                .build();
    }



    // Update editable fields in a medical record
    @Transactional
    public MedicalRecord updateMedicalRecord(Long id, MedicalRecord updatedRecord) {
        MedicalRecord existing = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MedicalRecord not found with id: " + id));

        if (updatedRecord.getMedications() != null && !updatedRecord.getMedications().isEmpty())
            existing.setMedications(updatedRecord.getMedications());
        if (updatedRecord.getFollowUpRequired() != null && !updatedRecord.getFollowUpRequired().isEmpty())
            existing.setFollowUpRequired(updatedRecord.getFollowUpRequired());

        existing.setNeedTherapy(updatedRecord.isNeedTherapy());
        return medicalRecordRepository.save(existing);
    }

    // Assign therapist to a record
    @Transactional
    public MedicalRecord assignTherapist(Long recordId, String therapistId) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Medical Record not found with id " + recordId));

        record.setTherapistId(therapistId);
        return medicalRecordRepository.save(record);
    }

    // Delete record
    public void deleteMedicalRecord(Long recordId) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Medical Record not found with ID: " + recordId));
        medicalRecordRepository.delete(record);
    }

    // Create record from patient request DTO
    public Boolean createFromPatientRequest(MedicalRecordRequestDTO dto) {
        Patient patient = patientRepository.findByUserId(dto.getPatientId());
        log.info("Patient : {}",patient);
        if(patient==null){
            log.error("Patient not found with id : {}",dto.getPatientId());
            return false;
        }

        MedicalRecord record = MedicalRecord.builder()
                .doctorId(dto.getDoctorId())
                .symptoms(dto.getSymptoms())
                .patient(patient)
                .medications(dto.getMedications())
                .followUpRequired(dto.getFollowUpRequired())
                .build();
        medicalRecordRepository.save(record);
        return true;
    }

    public List<MedicalRecordResponseDTO> findAllMedicalRecordsByDoctorId(String docId) {
	    return medicalRecordRepository.findByDoctorId(docId).stream().map(this::medicalRecordConvertToMedicalRecordResponseDTO).toList();
    }
}