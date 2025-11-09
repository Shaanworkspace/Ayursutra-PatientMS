package com.patientms.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private LocalDate dateOfBirth;
    private String bloodGroup;
    private String email;
    private String phoneNumber;
    private String address;
    private String emergencyContact;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private List<Integer> medicalRecordsIds;
}
