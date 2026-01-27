package com.patientms.Service;

import com.patientms.Client.DoctorClient;
import com.patientms.DTO.Request.MedicalRecordRequestDTO;
import com.patientms.DTO.Request.MedicalRecordTransfer;
import com.patientms.DTO.Request.TherapistRecordUpdateRequest;
import com.patientms.DTO.Response.MedicalRecordResponseDTO;
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
    private final DoctorClient doctorClient;
    private MedicalRecord record;

    @Transactional
    public MedicalRecord updateByTherapist(
            String recordId,
            TherapistRecordUpdateRequest req
    ) {
        MedicalRecord record = medicalRecordRepository
                .findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));

        if (req.getNeedTherapy() != null) {
            record.setNeedTherapy(req.getNeedTherapy());

            if (!req.getNeedTherapy()) {
                record.getTherapies().clear();
            }
        }

        if (req.getTherapies() != null && record.isNeedTherapy()) {
            record.setTherapies(req.getTherapies());
        }

        if (req.getTherapistNotes() != null) {
            record.setTherapistNotes(req.getTherapistNotes());
        }

        if (req.getSessionStatus() != null) {
            record.setSessionStatus(req.getSessionStatus());
        }
log.info("Updated medical record by therapist going to save : {}",record);
        return medicalRecordRepository.save(record);
    }

    // Update therapy requirements
    @Transactional
    public MedicalRecord updateTherapies(String recordId, boolean needTherapy, List<String> therapyIds) {
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
    public MedicalRecordResponseDTO getMedicalRecordById(String recordId) {
        MedicalRecord record = medicalRecordRepository.findByMedicalRecordId(recordId);
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
                .medicalRecordId(record.getMedicalRecordId())
                .patientName(record.getPatient().getName())
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
    @Transactional
    public void editMedicalRecord(
            String recordId,
            MedicalRecordRequestDTO dto
    ) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "MedicalRecord not found with id " + recordId
                        ));
        if (dto.getSymptoms() != null)
            record.setSymptoms(dto.getSymptoms());
        if (dto.getMedications() != null)
            record.setMedications(dto.getMedications());
        if (dto.getFollowUpRequired() != null)
            record.setFollowUpRequired(dto.getFollowUpRequired());
        if(dto.getTherapistId() != null)
            record.setTherapistId(dto.getTherapistId());
	    record.setNeedTherapy(dto.isNeedTherapy());
        if(dto.getPrescribedTreatment() !=null)
            record.setPrescribedTreatment(dto.getPrescribedTreatment());
        medicalRecordRepository.save(record);

        log.info("Saved Record new : {}",record);
    }

    public MedicalRecordTransfer medicalRecordConvertToMedicalRecordTransfer(
            MedicalRecord record
    ) {
        Patient patient = patientRepository.findByUserId(record.getPatient().getUserId());
        return MedicalRecordTransfer.builder()
                .medicalRecordId(record.getMedicalRecordId())
                .patientId(record.getPatient().getUserId())
                .doctorId(record.getDoctorId())
                .patientName(patient.getName())
                .therapistId(record.getTherapistId())
                .visitDate(record.getVisitDate())
                .createdDate(record.getCreatedDate())
                .build();
    }



    // Update editable fields in a medical record
    @Transactional
    public MedicalRecord updateMedicalRecord(String id, MedicalRecord updatedRecord) {
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
    public MedicalRecord assignTherapist(String recordId, String therapistId) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Medical Record not found with id " + recordId));

        record.setTherapistId(therapistId);
        return medicalRecordRepository.save(record);
    }

    // Delete record
    public void deleteMedicalRecord(String recordId) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Medical Record not found with ID: " + recordId));
        medicalRecordRepository.delete(record);
    }

    // Create record from patient request DTO
    public boolean addMedicalRecord(MedicalRecordRequestDTO dto) {
        Patient patient = patientRepository.findByUserId(dto.getPatientId());
        if(patient==null){
            log.error("Patient not found with id : {}",dto.getPatientId());
            throw new RuntimeException("No Patient");
        }
        log.info("Patient found for appointment post : {}",patient);
        MedicalRecord record = MedicalRecord.builder()
                .doctorId(dto.getDoctorId())
                .symptoms(dto.getSymptoms())
                .patient(patient)
                .medications(dto.getMedications())
                .followUpRequired(dto.getFollowUpRequired())
                .build();

        log.info("Medical Record Formed");

        MedicalRecord savedRecord = medicalRecordRepository.save(record);
        log.info("saved record in patient");
        return true;
    }

    public List<MedicalRecordResponseDTO> findAllMedicalRecordsByDoctorId(String docId) {
	    return medicalRecordRepository.findByDoctorId(docId).stream().map(this::medicalRecordConvertToMedicalRecordResponseDTO).toList();
    }

    public List<MedicalRecordResponseDTO> findAllMedicalRecordsByTherapistId(String therapistId) {
        return medicalRecordRepository.findByTherapistId(therapistId).stream().map(this::medicalRecordConvertToMedicalRecordResponseDTO).toList();
    }
}