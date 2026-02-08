package com.patientms.Service;

import com.patientms.Client.TherapistClient;
import com.patientms.DTO.Request.MedicalRecordRequestDTO;
import com.patientms.DTO.Response.MedicalRecordResponseDTO;
import com.patientms.DTO.Response.TherapyPlanDTO;
import com.patientms.ENUM.MedicalRecordStatus;
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
    private final TherapistClient therapistClient;


    public boolean updateMedicalRecord(String recordId, MedicalRecordRequestDTO recordRequestDTO) {
        MedicalRecord record = medicalRecordRepository.findById(recordId).orElse(null);
        if(record == null){
            log.warn("No medical record exist with medical id : {}",recordId);
            return false;
        }
        if(recordRequestDTO.isNeedTherapy()) record.setNeedTherapy(true);

        if(recordRequestDTO.getSessionMedicalRecordStatus() != null) {
            log.info("Medical Record id : {}, Status Updated to : {} ",recordId,record.getSessionMedicalRecordStatus());
            record.setSessionMedicalRecordStatus(recordRequestDTO.getSessionMedicalRecordStatus());
        }
        medicalRecordRepository.save(record);
        return true;
    }

    // Get all medical records as DTO list
    public List<MedicalRecordResponseDTO> getAllMedicalRecords() {
        return medicalRecordRepository.findAll()
                .stream()
                .map(this::medicalRecordConvertToMedicalRecordResponseDTO)
                .collect(Collectors.toList());
    }

    // Get record by ID (converted to DTO)
    public MedicalRecordResponseDTO getMedicalRecordByMedicalId(String recordId) {
        MedicalRecord record = medicalRecordRepository.findByMedicalRecordId(recordId);
        return medicalRecordConvertToMedicalRecordResponseDTO(record);
    }

    public List<MedicalRecordResponseDTO> getMedicalRecordsByIds(
            List<String> recordIds
    ) {
        return medicalRecordRepository.findAllById(recordIds)
                .stream()
                .map(this::medicalRecordConvertToMedicalRecordResponseDTO)
                .toList();
    }


    // Get records by patient
    public List<MedicalRecordResponseDTO> getMedicalRecordsByPatientId(String patientId) {
        return medicalRecordRepository.findMedicalRecordsByPatientId(patientId)
                .stream()
                .map(this::medicalRecordConvertToMedicalRecordResponseDTO)
                .collect(Collectors.toList());
    }

    // Get records by doctor
    public List<MedicalRecordResponseDTO> getMedicalRecordsByDoctorId(String doctorId) {
        return medicalRecordRepository.findByDoctorId(doctorId)
                .stream()
                .map(this::medicalRecordConvertToMedicalRecordResponseDTO)
                .collect(Collectors.toList());
    }

    public MedicalRecordResponseDTO medicalRecordConvertToMedicalRecordResponseDTO(
            MedicalRecord record
    ) {
        TherapyPlanDTO therapistPlans = therapistClient.getTherapyPlanByMedicalRecordId(record.getMedicalRecordId());
        log.info("Therapist plans fetched from by Therapist ms : {}",therapistPlans);
        Patient patient = patientRepository.findByUserId(record.getPatientId());
        return MedicalRecordResponseDTO.builder()
                .medicalRecordId(record.getMedicalRecordId())
                .therapistPlans(therapistPlans)
                .patientName(patient.getName())
                .patientId(patient.getUserId())
                .doctorId(record.getDoctorId())
                .visitDate(record.getVisitDate())
                .appointmentTime(record.getAppointmentTime())
                .updatedDateTime(record.getUpdatedDateTime())
                .doctorName(record.getDoctorName())
                .medications(record.getMedications())
                .visitDate(record.getVisitDate())
                .createdDateTime(record.getCreatedDateTime())
                .symptoms(record.getSymptoms())
                .prescribedTreatment(record.getPrescribedTreatment())
                .sessionMedicalRecordStatus(record.getSessionMedicalRecordStatus())
                .needTherapy(record.isNeedTherapy())
                .build();
    }


    @Transactional
    public MedicalRecord updateByTherapist(
            String recordId,
            MedicalRecordRequestDTO req
    ) {
        MedicalRecord record = medicalRecordRepository
                .findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));

        record.setNeedTherapy(req.isNeedTherapy());
        log.info("Updated medical record by therapist going to save : {}",record);
        return medicalRecordRepository.save(record);
    }
    @Transactional
    public MedicalRecord updateByDoctor(
            String recordId,
            MedicalRecordRequestDTO req
    ) {
        MedicalRecord record = medicalRecordRepository
                .findById(recordId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Medical record not found with id " + recordId
                        )
                );

        if (req.getSymptoms() != null) {
            record.setSymptoms(req.getSymptoms());
        }

        if (req.getPrescribedTreatment() != null) {
            record.setPrescribedTreatment(req.getPrescribedTreatment());
        }

        if (req.getMedications() != null) {
            record.setMedications(req.getMedications());
        }

        record.setNeedTherapy(req.isNeedTherapy());

        if (req.getVisitDate() != null) {
            record.setVisitDate(req.getVisitDate());
        }

        if (req.getAppointmentTime() != null) {
            record.setAppointmentTime(req.getAppointmentTime());
        }

        return medicalRecordRepository.save(record);
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

        if (dto.getPrescribedTreatment() != null)
            record.setPrescribedTreatment(dto.getPrescribedTreatment());

        if(dto.isNeedTherapy()){
            record.setNeedTherapy(true);
            record.setSessionMedicalRecordStatus(MedicalRecordStatus.THERAPY_RECOMMENDED);
        }else{
            record.setSessionMedicalRecordStatus(dto.getSessionMedicalRecordStatus());
        }
        if (dto.getVisitDate() != null)
            record.setVisitDate(dto.getVisitDate());
        if (dto.getMedications() != null)
            record.setMedications(dto.getMedications());
        if (dto.getAppointmentTime() != null)
            record.setAppointmentTime(dto.getAppointmentTime());

        MedicalRecord saved = medicalRecordRepository.save(record);
        log.info("MedicalRecord updated successfully: {}", saved.getMedicalRecordId());
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
            return false;
        }
        log.info("Patient found for appointment post : {}",patient);
        MedicalRecord record = MedicalRecord.builder()
                .doctorId(dto.getDoctorId())
                .doctorName(dto.getDoctorName())
                .symptoms(dto.getSymptoms())
                .patientId(patient.getUserId())
                .visitDate(dto.getVisitDate())
                .appointmentTime(dto.getAppointmentTime())
                .sessionMedicalRecordStatus(MedicalRecordStatus.CREATED)
                .build();


        log.info("Medical Record Formed");

        MedicalRecord savedRecord = medicalRecordRepository.save(record);
        log.info("saved record in patient");
        return true;
    }

    public List<MedicalRecordResponseDTO> findAllMedicalRecordsByDoctorId(String docId) {
	    return medicalRecordRepository.findByDoctorId(docId).stream().map(this::medicalRecordConvertToMedicalRecordResponseDTO).toList();
    }

}