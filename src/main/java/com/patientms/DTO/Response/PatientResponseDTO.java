package com.patientms.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDTO {
    private String userId;
    private String name;
    private String gender;
    private String phoneNumber;
    private List<Long> medicalRecordIds;
}
