package com.patientms.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordRequestDTO {
    private String patientId;              // ID of the patient booking the appointment
    private String doctorId;               // ID of the doctor being booked

    private LocalDate visitDate;     // Preferred appointment date & time
    private String symptoms;             // Description of symptoms or reason for visit
    private String allergies;            // Known allergies (if any)
    private String medicalHistoryNotes;  // Relevant past medical history
    private String medications;          // Current medications being taken
    private String followUpRequired;     // Yes/No/Maybe - if patient needs follow-up
}