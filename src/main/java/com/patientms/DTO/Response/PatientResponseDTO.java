package com.patientms.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponseDTO {
    private String userId;
    private String gender;
    private String phoneNumber;
    private List<MedicalRecordResponseDTO> medicalRecords;
}
