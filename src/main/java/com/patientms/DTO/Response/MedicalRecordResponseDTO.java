package com.patientms.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordResponseDTO {
    private String medicalRecordId;

    private String patientId;
    private String patientName;
    private String doctorId;
    private String therapistId;

    private LocalDate visitDate;
    private LocalDate createdDate;

    private String symptoms;
    private String prescribedTreatment;

    private String medications;
    private String followUpRequired;
    private boolean needTherapy;

    private boolean approvedByTherapist;
}
