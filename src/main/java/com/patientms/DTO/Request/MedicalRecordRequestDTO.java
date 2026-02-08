package com.patientms.DTO.Request;

import com.patientms.ENUM.MedicalRecordStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordRequestDTO {
    private String patientId;              // ID of the patient booking the appointment
    private String doctorId;               // ID of the doctor being booked
    private String doctorName;
    private String therapistName;
    private String therapistId;
    private String prescribedTreatment;
    private LocalTime appointmentTime;
    private LocalDate visitDate;     // Preferred appointment date & time
    private String symptoms;             // Description of symptoms or reason for visit
    private String allergies;            // Known allergies (if any)
    private String medications;
    private String followUpRequired;
    private MedicalRecordStatus sessionMedicalRecordStatus;
    private boolean needTherapy;
}